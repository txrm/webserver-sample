
object MetricsVisualization {
  def generateMetricsPage(metricsinput: String): String = {
    try {

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
          <pre>$metricsinput</pre>
        </body>
        </html>
        """

      html
    } catch {
      case ex: Exception =>
        s"Error generating metrics page: ${ex.getMessage}"
    }
  }
}
