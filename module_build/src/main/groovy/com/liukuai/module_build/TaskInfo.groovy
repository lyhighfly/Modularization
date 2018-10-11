package com.liukuai.module_build

class TaskInfo {
    String moduleName
    String taskType

    TaskInfo(String name, String type) {
        moduleName = name
        taskType = type
    }

    String getModuleName() {
        return moduleName
    }

    String getTaskType() {
        return taskType
    }

    boolean isAssemble() {
        return taskType != null && taskType.contains('assemble')
    }

    boolean isGenerate() {
        return taskType != null && taskType.startsWith("generate")
    }
}