package learnprogramming.academy

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import kotlin.properties.Delegates

private const val TAG = "DownloadData"

class DownloadData(private val callBack: DownloaderCallBack) : AsyncTask<String, Void, String>() {

    interface DownloaderCallBack {
        fun onDataAvailable(data: List<FeedEntry>)
    }

    override fun onPostExecute(result: String) {

        val parseApplications = ParseApplications()
        if(result.isNotEmpty()){
            parseApplications.parse(result)
        }

        callBack.onDataAvailable(parseApplications.applications)
    }

    override fun doInBackground(vararg url: String): String {
        Log.d(TAG, "doInBackground: starts with ${url[0]}")
        val rssFeed = downloadXML(url[0])
        if (rssFeed.isEmpty()) {
            Log.e(TAG, "doInBackground: Error downloading")
        }
        return rssFeed
    }

    private fun downloadXML(urlPath: String): String {
        try {
            return URL(urlPath).readText()
        } catch (e: MalformedURLException) {
            Log.e(TAG, "downloadXML: Invalid URL" + e.message)
        } catch (e: IOException) {
            Log.e(TAG, "downloadXML: IO Exception reading data" + e.message)
        } catch (e: SecurityException) {
            Log.e(TAG, "downloadXML: Security exception. Needs permission?" + e.message)
            e.printStackTrace()
        }
        return ""       //return an empty sting if there was an exception
    }
}