
package com.lxm.apkname

import org.gradle.api.Plugin
import org.gradle.api.Project
import com.android.build.gradle.AppExtension
import com.android.build.gradle.api.ApkVariantOutput
import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.api.BaseVariantOutput

public class ApkNamePlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        // 应用名称
        def projectName = project.name
        // 打包日期，格式为：年月日时分秒
        def buildTime = new Date().format("yyyy-MM-dd-HH-mm-ss")
        // project有很多扩展信息，我们这里需要Android相关的扩展信息，com.android.build.gradle.AppExtension
        def appExtension = project.extensions.getByType(AppExtension)
        // 遍历扩展信息里的所有变体，根据每个变体的信息组合出Apk的文件名称
        appExtension.applicationVariants.all { variant ->
            // 通过方法定义我们知道variant的实际类型为ApplicationVariant
            // val applicationVariants: DomainObjectSet<ApplicationVariant> =
            //         dslServices.domainObjectSet(ApplicationVariant::class.java)
            def versionCode = ((ApplicationVariant) variant).versionCode
            def versionName = 'v' + ((ApplicationVariant) variant).versionName
            def buildType = ((ApplicationVariant) variant).buildType.name
            def flavor = ((ApplicationVariant) variant).flavorName

            ((ApplicationVariant) variant).outputs.all { output ->
                // 变体输出的除了apk还有其他文件，这里我们只修改apk的文件名
                def outputFile = ((BaseVariantOutput) output).outputFile
                if (outputFile != null && outputFile.name.endsWith(".apk")) {
                    ((ApkVariantOutput) output).outputFileName = "$versionName-$flavor-$buildType-${buildTime}.apk"
                }
            }
        }
    }
}