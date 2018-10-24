trait TeacherRepository {
  def getRandomly(): String
}

class TeacherRepositoryImpl extends TeacherRepository {
  override def getRandomly(): String = "風見先生"
}

object Main {

  class TeacherSelectService_pure_scala(teacherRepository: TeacherRepository) {
    def printYourTeacher(): Unit = {
      println("あなたの先生は " + teacherRepository.getRandomly())
    }
  }

  import com.google.inject.Inject
  class TeacherSelectService_google_guice @Inject() (teacherRepository: TeacherRepository) {
    def printYourTeacher(): Unit = {
      println("あなたの先生は " + teacherRepository.getRandomly())
    }
  }

  class TeacherSelectService_airframe(teacherRepository: TeacherRepository) {
    def printYourTeacher(): Unit = {
      println("あなたの先生は " + teacherRepository.getRandomly())
    }
  }

  class TeacherSelectService_macwire(teacherRepository: TeacherRepository) {
    def printYourTeacher(): Unit = {
      println("あなたの先生は " + teacherRepository.getRandomly())
    }
  }


  def main(args: Array[String]): Unit = {
    // pure scala
    {
      new TeacherSelectService_pure_scala(new TeacherRepositoryImpl).printYourTeacher()
    }

    // google guice
    // コードの参考ページ: https://qiita.com/saka1_p/items/45fdf1f736173fcf1c5a
    {
      import com.google.inject._

      class RealModule extends AbstractModule {
        override protected def configure(): Unit = {
          bind(classOf[TeacherRepository]).to(classOf[TeacherRepositoryImpl])
        }
      }

      val injector = Guice.createInjector(new RealModule)

      injector.getInstance(classOf[TeacherSelectService_google_guice]).printYourTeacher()
    }

    // airframe
    // コードの参考ページ: https://wvlet.org/airframe/docs/index.html#constructor-injection
    {
      import wvlet.airframe._

      val design = newDesign
        .bind[TeacherRepository].toSingletonOf[TeacherRepositoryImpl]

      design.build[TeacherSelectService_airframe] { teacherSelectService =>
        teacherSelectService.printYourTeacher()
      }
    }

    // macwire
    // コードの参考ページ: https://github.com/adamw/macwire#macwire
    {
      import com.softwaremill.macwire._

      class Module {
        lazy val teacherRepository: TeacherRepository = wire[TeacherRepositoryImpl]
        lazy val teacherSelectService: TeacherSelectService_macwire = wire[TeacherSelectService_macwire]
      }

      new Module().teacherSelectService.printYourTeacher()
    }

  }
}
