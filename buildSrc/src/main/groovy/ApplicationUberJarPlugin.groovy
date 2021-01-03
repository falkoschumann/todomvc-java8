import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.distribution.Distribution
import org.gradle.api.distribution.plugins.DistributionPlugin
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.bundling.Jar

class ApplicationUberJarPluginExtension {
  String mainClassName
}

class ApplicationUberJarPlugin implements Plugin<Project> {
  void apply(Project project) {
    project.pluginManager.apply('todomvc.java-common-conventions')
    project.pluginManager.apply('distribution')

    def extension = project.extensions.create('application', ApplicationUberJarPluginExtension)
    project.tasks.register('run', JavaExec) {
      group = 'application'
      main = extension.mainClassName
      classpath = project.convention.getPlugin(JavaPluginConvention).sourceSets.main.runtimeClasspath
    }

    def uberJar = project.tasks.register('uberJar', Jar) {
      group = 'distribution'
      archiveClassifier = 'uber'
      manifest {
        attributes(
          'Implementation-Title': 'TodoMVC',
          'Implementation-Version': archiveVersion,
          'Implementation-Vendor': 'Falko Schumann <falko.schumann@muspellheim.de>',
          'Main-Class': extension.mainClassName
        )
      }
      from project.convention.getPlugin(JavaPluginConvention).sourceSets.main.output
      dependsOn project.configurations.runtimeClasspath
      from {
        project.configurations.runtimeClasspath.findAll { it.name.endsWith('jar') }.collect { project.zipTree(it) }
      }
    }

    def distributions = project.getExtensions().getByName("distributions");
    Distribution mainDistribution = distributions.getByName(DistributionPlugin.MAIN_DISTRIBUTION_NAME);
    mainDistribution.contents {
      from(uberJar)
      from(project.rootDir) {
        include 'README.md'
        include 'LICENSE.txt'
        include 'CHANGELOG.md'
        rename 'md', 'txt'
      }
    }
  }
}
