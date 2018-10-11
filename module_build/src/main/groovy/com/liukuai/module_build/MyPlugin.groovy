package com.liukuai.module_build

import org.gradle.api.Plugin
import org.gradle.api.Project
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin

//只要声明使用该插件，则每个project被编译时，都会调用
class MyPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        //注意不能在task内部注册该监听

        def main_module = project.rootProject.ext.main_module
        def this_module = project.getName()
        println "apply project modulea plugin name:${project}:::main_module:${main_module}: this_module:${this_module}"
        List<String> taskName = project.gradle.startParameter.taskNames
        println "All Task : ${taskName}"

        //此处考虑欠妥，有许多场景没有考虑
        //如果task大于一项，则表示是sync动作，则将所有的module都设置为Application
        if (taskName == null || taskName.size() == 0 || (taskName.size() == 1 && taskName.get(0).contains("clean"))) {
            println "only clean build folder, sync apply application plugin"
            project.apply plugin: 'com.android.application'
            project.extensions.create('appindicator', AppIndicator)
//            if (this_module.equals(main_module)) {
//                println "sync main"
//                useReleaseSourceSets(project)
//            } else {
            println "sync module"
            useDebugSourceSets(project)
            return
        }
        List<TaskInfo> taskList = new ArrayList<>()
        boolean hasSyncAction = false
        for (String name : taskName) {
            TaskInfo info = getBuildTaskInfo(name)
            if (info.isGenerate()) {
                hasSyncAction = true
            }
            taskList.add(info)
        }

        if (hasSyncAction) {
            println "gradle Sync Action, sync apply application plugin"
            project.apply plugin: 'com.android.application'
            project.extensions.create('appindicator', AppIndicator)
            useDebugSourceSets(project)
            return
        }

        TaskInfo taskInfo = getBuildTaskInfo(taskName.get(0))
        if (taskInfo == null) {
            println "currentTask: ${taskName.get(0)}, taskInfo is null,,, return"
            return
        }
        String moduleName = taskInfo.moduleName
        for (TaskInfo info : taskList) {
            if (!info.moduleName.equals(moduleName)) {
                println "one line command only contains one module!!!!!!!!!!!!!!!!!!!!!! BREAK"
                return
            }
        }
        println "taskinfo :  name: ${taskInfo.moduleName} , task: ${taskInfo.taskType}"

        if (!taskInfo.moduleName.equals(this_module) && taskInfo.isAssemble()) {
            def mainProject = project.project(':' + taskInfo.moduleName)
            String[] mainDepenModuleNames = getDependenciesName(mainProject.properties.get("release_dependency_module"))
            println " mainProject depens: ${mainDepenModuleNames}"
            //如果主工程依赖此工程，早晚此工程会被设置，所以就跳过此处的设置，等待主工程的设置过程
            if (mainDepenModuleNames.contains(this_module))
                return
            boolean hasAndroidPlugin = hasAndroidPlugin(project)
            println "this module have android plugin:?${hasAndroidPlugin}"
            if (!hasAndroidPlugin) {
                println "this module has not yet set android plugin, so set application plugin"
                project.apply plugin: 'com.android.application'
                project.extensions.create('appindicator', AppIndicator)
//                if (this_module.equals(main_module)) {
//                    println "sync main"
//                    useReleaseSourceSets(project)
//                } else {
                println "sync module"
                useDebugSourceSets(project)
//                }
            }
            return
        }

        boolean hasAndroidPlugin = hasAndroidPlugin(project)
        if (hasAndroidPlugin) {
            return
        }
        if (taskInfo.moduleName.equals(this_module)) {
            println "config main module"
            project.apply plugin: 'com.android.application'
            project.extensions.create('appindicator', AppIndicator)
//            if (this_module.equals(main_module)) {
//                println "sync main"
//                useReleaseSourceSets(project)
//            } else {
            println "sync module"
            useDebugSourceSets(project)
//            }
            //extensions的内容必须在configuration之后才能获得
            project.afterEvaluate {
                println "now can get ext value:${project.appindicator.myRealApp}"
            }
            String[] depenItems = getDependenciesName(project.properties.get("release_dependency_module"))
            println "main_module:${main_module}: depen:${depenItems}"
            for (String pro : depenItems) {
                println "add project1 ${pro}"
                def libraryProject = project.project(':' + pro)
                libraryProject.apply plugin: 'com.android.library'
                libraryProject.extensions.create('appindicator', AppIndicator)
                //必须在声明了应用plugin之后，才能调用dependencies添加依赖
                project.dependencies.add('api', libraryProject)
                println "library project ${libraryProject}"
                useReleaseSourceSets(libraryProject)
                println "library project ${libraryProject} config finish"
            }

            if (this_module.equals(main_module)) {
                println "main mudule register transform"
                project.android.registerTransform(new AppTransform(project, depenItems))
            }
        } else {
            println "config sub module"
            project.apply plugin: 'com.android.library'
            useReleaseSourceSets(project)
        }

    }

    void useDebugSourceSets(Project project) {
        project.android.sourceSets {
            main {
                manifest.srcFile 'src/main/debug/AndroidManifest.xml'
                java.srcDirs = ['src/main/debug/java', 'src/main/java']
                res.srcDirs = ['src/main/debug/res', 'src/main/res']
                assets.srcDirs 'src/main/assets'
                jniLibs.srcDirs 'src/main/jniLibs'
            }
        }
    }

    void useReleaseSourceSets(Project project) {
        project.android.sourceSets {
            main {
                manifest.srcFile 'src/main/AndroidManifest.xml'
                java.srcDirs 'src/main/java'
                res.srcDirs 'src/main/res'
                assets.srcDirs 'src/main/assets'
                jniLibs.srcDirs 'src/main/jniLibs'
            }
        }
    }

    boolean hasAndroidPlugin(Project project) {
        return project.getPlugins().hasPlugin(AppPlugin.class) || project.getPlugins().hasPlugin(LibraryPlugin.class)
    }

    private String[] getDependenciesName(String depenStr) {
        if (depenStr == null || depenStr.length() == 0)
            return new String[0]
        String[] strs = depenStr.split(',')
        return strs
    }

    private TaskInfo getBuildTaskInfo(String firstTaskName) {
        if (firstTaskName == null || firstTaskName.length() == 0)
            return null
        boolean assignModule = firstTaskName.contains(':')
        if (assignModule) {
            String[] strs = firstTaskName.split(':')
            if (strs.length < 3)
                return null
            return new TaskInfo(strs[1], strs[2])
        } else {
            return new TaskInfo("app", firstTaskName)
        }
    }
}
