import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.gradle.api.Project

class AppTransform extends Transform {

    private Project project
    private String[] depenNames
    private String[] moduleNames
    private String mainAppName
    private ClassPool classPool

    AppTransform(Project pro, String[] depenModuleNames) {
        project = pro
        depenNames = depenModuleNames
        println "---------------init AppTransform ${project}"
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        println "---------------init AppTransform : ${project.extensions.appindicator.myRealApp}"
        mainAppName = project.extensions.appindicator.myRealApp
        moduleNames = new String[depenNames.length]
        for (int i = 0; i < depenNames.length; i++) {
            Project libProject = project.project(':' + depenNames[i])
            println "---------------init  add ModuleApp:${libProject.extensions.appindicator.myShadowApp}"
            moduleNames[i] = libProject.extensions.appindicator.myShadowApp
        }

        println "use custom transform~~~~~~~~~~~~~~"
        classPool = new ClassPool()
        def allClass =  ConvertUtils.toCtClasses(transformInvocation.getInputs(), classPool)
        /**The default class pool searches the system search path,
        * which usually includes the platform library, extension
        * libraries, and the search path specified by the
        * <code>-classpath</code> option or the <code>CLASSPATH</code>
        * environment variable.看来自定义java是没有在这些路径之中的**/
//        classPool = ClassPool.getDefault()

        List<CtClass> moduleAppClazz = new ArrayList<>()
        for(String moduleName : moduleNames){
            moduleAppClazz.add(classPool.get(moduleName))
        }
        println "${mainAppName}-------------add All module Class: ${moduleAppClazz}"

        def mainAppShortName = mainAppName.substring(mainAppName.lastIndexOf('.')+1)
        println "main short Name:${mainAppShortName}"
        transformInvocation.inputs.each { TransformInput input ->
            //对类型为jar文件的input进行遍历
            input.jarInputs.each { JarInput jarInput ->
                //jar文件一般是第三方依赖库jar文件
                // 重命名输出文件（同目录copyFile会冲突）
                def jarName = jarInput.name
                println "jarName:${jarName}"
                def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
                if (jarName.endsWith(".jar")) {
                    jarName = jarName.substring(0, jarName.length() - 4)
                }
                //生成输出路径
                def dest = transformInvocation.outputProvider.getContentLocation(jarName + md5Name,
                        jarInput.contentTypes, jarInput.scopes, Format.JAR)
                //将输入内容复制到输出
                FileUtils.copyFile(jarInput.file, dest)

            }
            //对类型为“文件夹”的input进行遍历
            input.directoryInputs.each { DirectoryInput directoryInput ->
                String fileName = directoryInput.file.absolutePath
                File dir = new File(fileName)
                dir.eachFileRecurse { File file ->
                    String filePath = file.absolutePath
                    println "filePath:${filePath}"
//                    if(filePath.contains(mainAppShortName)) {
//                        String classNameTemp = filePath.replace(fileName, "")
//                                .replace("\\", ".")
//                                .replace("/", ".")
                    //不严谨，可能出现DevelopCashierApplication.class
                    if (filePath.endsWith("/" + mainAppShortName + ".class") && moduleAppClazz.size() > 0) {
                            println "------------------find MainApp inject code"
                            CtClass mainAppClass = classPool.get(mainAppName)
                            injectModuleAppCode(mainAppClass, moduleAppClazz, fileName)
                        }
//                    }
                }
                def dest = transformInvocation.outputProvider.getContentLocation(directoryInput.name,
                        directoryInput.contentTypes,
                        directoryInput.scopes, Format.DIRECTORY)
                // 将input的目录复制到output指定目录
                FileUtils.copyDirectory(directoryInput.file, dest)
            }
        }
    }

    private void injectModuleAppCode(CtClass mainClass, List<CtClass> moduleClazz, String path){
        println "begin inject Module App code :${mainClass.getName()}"
        mainClass.defrost()
        try{
            CtMethod mainCreateMethod = mainClass.getDeclaredMethod("onCreate")
            StringBuilder builder = new StringBuilder()
            println "begin inject Module App code 1"
            //因为android.jar不在classPool的classpath搜索路径内，要使用this，就需要将android的内容添加到搜索路径中
            def androidPath = getSDkJarPath()
            println "androidPath:${androidPath}"
            classPool.insertClassPath(androidPath)
            for(CtClass moduleClass : moduleClazz){
                builder.append("new " + moduleClass.getName() + "().onCreate(this);")
            }
            mainCreateMethod.insertAfter(builder.toString())
        }catch(Exception e){
            println '$$$$$$$$$$$$$$$$$$$$$$$$$$$$$'+"happen Exception: ${e.getMessage()}"
        }
        mainClass.writeFile(path)
        mainClass.detach()
        println "end inject Module App code"
    }

    private String getSDkJarPath(){
        def compile_sdk_version = project.android.compileSdkVersion
        def jarPath = project.android.sdkDirectory.getPath()+"/platforms/"+compile_sdk_version+"/android.jar"
        return jarPath
    }

    @Override
    String getName() {
        return "ModuleApp"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }
}