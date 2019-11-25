package service

import java.time.format.DateTimeFormatterBuilder
import java.time.{LocalDateTime, ZoneOffset}

import example.googlehistory.GoogleHistoryValue
import parameters.Config

import scala.io.Source
import scala.xml.Node

trait ParseGoogleKml {
  private val formatter = new DateTimeFormatterBuilder()
    .appendPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    .toFormatter()

  def parse(config: Config, userId: String): List[GoogleHistoryValue] = {

    def parseCoordinates(p: Node): List[(Double, Double)] = {

      val coordinates =
        if (!(p \\ "Point" \\ "coordinates").text.isEmpty) {
          (p \\ "Point" \\ "coordinates").text
        } else {
          (p \\ "LineString" \\ "coordinates").text
        }

      coordinates
        .split(",0 ")
        .map(x => (x.split(",")(1).toDouble, x.split(",")(0).toDouble))
        .toList

    }

    val googleTimeline = Source.fromFile(config.filePath).mkString
    val history = scala.xml.XML.loadString(googleTimeline)
    val placeMarks = (history \\ "kml" \\ "Document" \\ "Placemark").toList

    placeMarks.flatMap { p =>
      val activity = (p \\ "name").text

      val startTime = LocalDateTime.parse((p \\ "TimeSpan" \\ "begin").text, formatter).toInstant(ZoneOffset.UTC)
      val endTime = LocalDateTime.parse((p \\ "TimeSpan" \\ "end").text, formatter).toInstant(ZoneOffset.UTC)

      val coordinates = parseCoordinates(p)

      coordinates
        .map { case (lat, lon) =>
          GoogleHistoryValue(user_id = userId, startTime, endTime, activity, lat, lon)
        }
    }
  }
}

object ParseGoogleKml extends ParseGoogleKml