import org.gradle.api.Project
import org.gradle.api.Plugin

class JavacardPlugin implements Plugin<Project> {
    
    void apply(Project project) {

        project.sourceCompatibility = '1.2'
        project.targetCompatibility = '1.2'

        project.extensions.create("cap", CapExtension)
    
        project.task('capfiles') {

            ext.capBuildDir = new File(project.buildDir, 'capfiles')

            doLast {


            

                def destinationDir = 'build/classes/main'

                def javacardHome = '/home/yves/opt/java-card-sdk'
                def translate = [
                    "${javacardHome}/bin/converter",
                    '-out CAP',
                    '-d',  capBuildDir,
                    '-classdir', project.compileJava.destinationDir,
                    '-applet', "${project.cap.aid}:0x01", project.cap.app,
                    '-exportpath', "${javacardHome}/api_export_files",
                    project.cap.getAppPackagePath(),
                    project.cap.aid,
                    project.cap.version ].join(' ')

                def proc = translate.execute() 
                proc.waitFor()
                assert(proc.exitValue() == 0)
            }
        }
    }
}

class CapExtension {
    String aid
    String app
    String version

    String getAppClassName() {
        this.app.findAll('[^.]+')[-1]
    }

    def getAppPackagePath() {
        this.app.findAll('[^.]+')[0..-2].join('.')
    }


}
