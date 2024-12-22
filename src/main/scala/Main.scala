import smithy4s.http4s.*
import smithy4s.decline.Smithy4sCli
import cats.effect.*
import com.monovore.decline.*
import com.monovore.decline.effect.CommandIOApp
import org.http4s.ember.client.EmberClientBuilder
import cats.effect.kernel.Resource
import org.http4s.client.Client
import org.http4s.Uri
import epollcat.EpollApp

object Main extends EpollApp {

  private val http4sClient: Resource[IO, Client[IO]] =
    EmberClientBuilder.default[IO].build

  private val numbersClient = http4sClient.flatMap { baseClient =>
    SimpleRestJsonBuilder(cli.Numbers)
      .client(baseClient)
      .uri(Uri.unsafeFromString("http://numbersapi.com"))
      .resource
  }

  private def createCli(args: List[String]): IO[ExitCode] = numbersClient.use {
    client =>
      val command = Smithy4sCli
        .standalone(Opts(client))
        .command
        .map(
          _.redeem(_ => ExitCode.Error, _ => ExitCode.Success)
        )

      CommandIOApp.run(command, args)
  }

  def run(args: List[String]): IO[ExitCode] = {
    createCli(args)
  }
}
