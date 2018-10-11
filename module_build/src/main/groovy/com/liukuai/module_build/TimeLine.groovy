import org.gradle.BuildListener
import org.gradle.BuildResult
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.execution.TaskExecutionListener
import org.gradle.api.initialization.Settings
import org.gradle.api.invocation.Gradle
import org.gradle.api.tasks.TaskState


class TimeLine implements TaskExecutionListener, BuildListener{

    private  clock
    private times = []
    private mainapp

    public TimeLine(Project project){
        mainapp = project.rootProject.ext.main_module
    }
    @Override
    void buildStarted(Gradle gradle) {
        println "buildStarted:"

    }

    @Override
    void settingsEvaluated(Settings settings) {
        println "settingsEvaluated:"
    }

    @Override
    void projectsLoaded(Gradle gradle) {
        println "projectsLoaded:"
    }

    @Override
    void projectsEvaluated(Gradle gradle) {
        println "projectsEvaluated:"
    }

    @Override
    void buildFinished(BuildResult buildResult) {
        println "buildFinished:"
        for(time in times){
            if (time[0] >= 50){
                printf("%7sms  %s\n", time)
            }
        }
    }

    @Override
    void beforeExecute(Task task) {
        println "main module is ${mainapp}"
        if(mainapp.equals(task.getProject().getName())) {
            println "beforeExecute: init clock :${task.getName()} ----- ${task.getProject()}"
            clock = System.currentTimeMillis()
        }
    }

    @Override
    void afterExecute(Task task, TaskState taskState) {
        println "afterExecute:"
        long ms = System.currentTimeMillis() - clock
        times.add([ms, task.path])
    }
}