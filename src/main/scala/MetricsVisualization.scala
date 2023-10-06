import play.api.libs.json.{Json, JsValue}
import play.api.libs.json.Json.prettyPrint

object MetricsVisualization {
  def generateMetricsPage(metricsJson: String): String = {
    try {
      val prettyMetrics = prettifyMetrics(metricsJson)

      // HTML and CSS code for the visualization
      val html =
        s"""
        <!DOCTYPE html>
        <html>
        <head>
          <title>System Metrics</title>
          <style>
            body {
              font-family: Arial, sans-serif;
              margin: 20px;
            }
            h1 {
              color: #333;
            }
            pre {
              background-color: #f0f0f0;
              padding: 10px;
              border-radius: 5px;
            }
          </style>
        </head>
        <body>
          <h1>System Metrics</h1>
          <pre>$prettyMetrics</pre>
        </body>
        </html>
        """

      html
    } catch {
      case ex: Exception =>
        s"Error generating metrics page: ${ex.getMessage}"
    }
  }

  private def prettifyMetrics(metricsJson: String): String = {
    try {
      val json: JsValue = Json.parse(metricsJson)

      val prettyMetrics: String = prettyPrint(json)

      prettyMetrics
    } catch {
      case ex: Exception =>
        s"Error formatting and prettifying metrics: ${ex.getMessage}"
    }
  }
}
