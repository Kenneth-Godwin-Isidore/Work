import com.fasterxml.jackson.dataformat.csv.CsvSchema
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File
import java.io.FileWriter
import com.typesafe.config.ConfigFactory
object CSVTOJSON {
  def main(args: Array[String]): Unit = {
    def csvFileToJson(filePath: String): String = {
      val inputCsvFile = new File(filePath)

      // if the csv has header, use setUseHeader(true)
      val csvSchema = CsvSchema.builder().setUseHeader(true).build()
      val csvMapper = new CsvMapper()

      // java.util.Map[String, String] identifies they key values type in JSON
      val readAll = csvMapper
        .readerFor(classOf[java.util.Map[String, String]])
        .`with`(csvSchema)
        .readValues(inputCsvFile)
        .readAll()

      val mapper = new ObjectMapper()

      // json return value
      mapper.writerWithDefaultPrettyPrinter().writeValueAsString(readAll)
    }
    val confFile=ConfigFactory.load()
    val inputFilepath=confFile.getString("App.file.input")
    val a = csvFileToJson(inputFilepath)
    val outputFilepath=confFile.getString("App.file.output")
    val file = new FileWriter(outputFilepath)
    file.write(a)
    file.close()
  }
}