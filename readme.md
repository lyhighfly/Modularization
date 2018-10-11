# 以下是我在写gradle plugin时候的自言自语，请忽略。

# 具体工程结构，和一些组件化的想法在[组件化实践](https://lyhighfly.github.io/2018/07/14/%E7%BB%84%E4%BB%B6%E5%8C%96%E5%AE%9E%E8%B7%B5/)

1, 编译gradle插件注意事项：
插件module的文件结构必须是固定的，要不然apply plugin的时候是找不到插件信息的

在根目录中的build.gradle中，需要指定使用本地版本库mavenLocal()

对于插件库的依赖在根目录下的build.gradle中添加 classpath

具体使用插件的mudule中的build.gradle中，apply plugin 指定的插件索引为插件module中resources目录下properties文件的文件名，不含后缀信息

有多少个build.gradle，就有多少个project，是一一对应的关系

执行具体module的具体task： gradle :module:task

编写gradle脚本的体会：注意类的import：1，防止类导入错误；2，注意外部包类的引入，因为即使没有引入，脚本编辑器也不会报错，只有运行时才能发现错误

感觉家里的gradle版本和公司的不太相同，公司的gradle执行task，不会连带执行其他task。家里的因为连带执行，导致修改和测试plugin会麻烦，在uploadArchives执行时需要屏蔽module对其的使用

gradle的指定扩展属性，在build.gradle中定义完成之后，就必须在plugin中有相应的处理，不然就会build报错:can`t find method [扩展属性名称]
build.gradle可以没有扩展属性，但是，如果有，必须做相应的读取工作

虽然类有路径，所以不需要像资源一样，规定前缀来达到资源隔离。但是在ARouter使用时，还是会因为不同module下同一个包名而引起duplicate classes with the same name的问题
不同module下的包名也要保持不一样，最好以module name为前缀(最起码使用ARouter产生路由的那些类的包名应该不重复)

ARouter生成中间路由类会有缓存，所以在更改了路由path之后，一定要clean，再build

执行某个具体module的build工作，也会执行所有声明使用自定义plugin的其他module，其顺序依赖于setting.bundle中定义的顺序(猜测错误)