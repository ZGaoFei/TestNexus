1、创建一个model，然后将此model打包成aar文件，然后上传到自己本地的maven仓库中

2、在项目所在的gradle.properties中添加全局的设置如下：（也可选择在model项目下的gradle.properties）

    # 包信息，根据model的包名来设置
    PROJ_GROUP=com.example.utillibrary
    # 版本号
    PROJ_VERSION=1.0

    # 固定参数，包含一些协议等信息
    # Licence信息
    PROJ_LICENCE_NAME=The Apache Software License, Version 2.0
    PROJ_LICENCE_URL=http://www.apache.org/licenses/LICENSE-2.0.txt
    PROJ_LICENCE_DEST=repo

    # 项目信息
    # Developer 信息
    DEVELOPER_ID=com.example.utillibrary
    DEVELOPER_NAME=utillibrary

    # 生成本地maven仓库的位置，文件夹所在的位置，
    # 表示项目所在根目录，并在此根目录下创建home/lv/.m2/repository/文件路径
    LOCAL_REPO_URL=file:///home/lv/.m2/repository/
    Windows默认本地maven仓库为C:\Users\用户\.m2\repository

3、在model所在的build.gradle中添加事件用于生成aar文件

    apply plugin: 'maven'
    uploadArchives {
        repositories.mavenDeployer {
            repository(url: LOCAL_REPO_URL)
            pom.groupId = PROJ_GROUP
            pom.artifactId = PROJ_ARTIFACTID
            pom.version = PROJ_VERSION
        }
    }

4、在项目所在路径下执行命令

    命令：gradlew -p 自己命名的仓库名 clean build uploadArchives --info
    # 两种方式，一种是直接在as的Terminal中运行，另一种是命令行进入项目文件路径运行

5、生成成功后会在相应的路径看到对应的文件

    文件包含有以下文件：

    localrepo
    │   │           ├── 1.0.0
    │   │           │   ├── localrepo-1.0.0.aar
    │   │           │   ├── localrepo-1.0.0.aar.md5
    │   │           │   ├── localrepo-1.0.0.aar.sha1
    │   │           │   ├── localrepo-1.0.0.pom
    │   │           │   ├── localrepo-1.0.0.pom.md5
    │   │           │   └── localrepo-1.0.0.pom.sha1
    │   │           ├── maven-metadata.xml
    │   │           ├── maven-metadata.xml.md5
    │   │           └── maven-metadata.xml.sha1

6、引用本地库（两种引用方式）

    # 第一种是直接将aar文件导入项目的lib文件夹下，在app下的build.gradle与android{}同等级下添加引用lib下的库

        repositories {
            flatDir {
                dirs 'libs'
            }
        }

     # 在dependencies中添加引用

        compile(name:'utillibrary-debug', ext:'aar')

    # =====================================

    # 第二种是通过引用本地maven库的形式进行引用

    # 在项目的build.gradle中添加本地maven路径，在buildscript下和allprojects下均添加下面的内容：

        repositories {
                maven { url 'E:/home/lv/.m2/repository/' }// 文件路径为上面生成的路径
                jcenter()
            }

     # 引用，在app的dependencies中添加以下依赖（根据生成的库自行修改）

        compile 'com.example.utillibrary:utillibrary:1.0'

7、须知：

    http://blog.csdn.net/fyfcauc/article/details/70174960

    http://blog.bugtags.com/2016/01/27/embrace-android-studio-maven-deploy/

    https://blog.csdn.net/jinyp/article/details/55095310

    路径问题：http://www.it1352.com/146505.html

==================分割线==========================

#### Nexus的使用和本地lib上传nexus仓库和引用nexus仓库里面的aar库

Nexus 私服安装和打开过程（以Nexus3.9.0）

1、下载压缩包并解压到指定目录（自己选择）

2、进入解压后的文件目录nexus-3.9.0-01-win64\nexus-3.9.0-01\bin下，
以管理员的身份进入，在开始附件里面右键选择管理员身份启动命令行工具，进入此目录下

3、安装、运行、开启服务

	命令行：
		安装：nexus.exe /install nexus
			安装后会显示：Installed service ‘nexus’

		运行：nexus.exe /start nexus
			运行后会显示：Starting service 'nexus'
			此项可以在任务管理器的服务中查看服务nexus项是否启动

		开启服务：net start nexus
			运行后会显示：nexus 服务正在启动 . nexus 服务已经启动成功

		关闭：nexus.exe /stop nexus

4、在浏览器输入：http://localhost:8000/，如果能访问则表示成功

5、问题：

	nexus的默认端口为8081，这个端口可能会被占用，这时可能无法打开，会显示403

	在nexus-3.9.0-01-win64\nexus-3.9.0-01\etc\nexus-default.properties里修改端口号为8000

	重新启动nexus，后刷新浏览器

6、登录

    默认登录
    账号：admin
    密码：admin123

7、上传

    上传和本地maven库的使用一致，只是在其基础上添加一个登录验证

    在model所在的build.gradle中添加事件用于生成aar文件

        apply plugin: 'maven'
        uploadArchives {
            repositories.mavenDeployer {
                    repository(url: LOCAL_REPO_URL) {
                        // 添加验证，账号和密码
                        authentication(userName: "admin", password: "admin123")
                    }
                    pom.groupId = PROJ_GROUP
                    pom.artifactId = PROJ_ARTIFACTID
                    pom.version = PROJ_VERSION
                }
        }

        注意：需要注意的是LOCAL_REPO_URL，LOCAL_REPO_URL对应的是生成的aar文件上传的位置
        LOCAL_REPO_URL可以是本地maven仓库地址，也可以是nexus私服地址，或者管理的远端代码仓库地址
        这里使用的私服地址

8、引用

    在项目根目录的build.gradle文件下添加nexus上的maven仓库地址
    // 本地私服地址（根据代码上传地址定）
    maven { url 'http://127.0.0.1:8000/repository/maven-releases/' }

    在APP下的build.gradle文件下进行引用
    compile 'com.example.testlibrary:testlibrary:1.1'


9、问题

    上传到nexus中maven仓库里的aar包，里面不可包含其他aar包（亲测不可用），
    aar包里面如果引用有远端maven仓库里的其他包，
    需要添加maven仓库的地址（maven { url 'url' }，可能是maven仓库也可能是自己维护的maven仓库），
    否则找不到引用。
    在更新nexus仓库里面的aar包时，需要修改一下版本号，不然更新失败

10、上传与混淆

    如果代码有区分debug和release，在上传的时候，系统会自动选取release的配置进行上传
    需要混淆的话，可以在release的配置文件中添加上混淆的文件即可，系统会自动处理。

> 测试的项目代码为 TestMavenLibrary

====================分割线===============================

#### 总结

1、加载lib目录下的aar包

    compile(name:'utillibrary-debug', ext:'aar')

2、加载本地maven仓库的aar包

    根build.gradle:maven { url 'E:/home/lv/.m2/repository/' }
    app下build.gradle:compile 'com.example.utillibrary:utillibrary:1.0'

3、加载远端nexus代码仓库的aar包

    根build.gradle:maven { url 'http://127.0.0.1:8000/repository/maven-releases/' }
    app下build.gradle:compile 'com.example.testlibrary:testlibrary:1.1'
