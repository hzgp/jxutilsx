package com.jxkj.utils

import android.annotation.TargetApi
import android.os.Build
import org.w3c.dom.Document
import org.xml.sax.SAXException
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.DecimalFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException

/**
 * Created by ZQiong on 2018/10/24.
 * 伟大的创作，一个dimens.xml生成器
 */
object DPGeneratorUtils {
    private const val HEAD = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" //头部
    private const val START_TAG = "<resources>\n" //开始标签
    private const val END_TAG = "</resources>\n" //结束标签
    private const val DP_BASE = 360f //360dp为基准
    private val dps = intArrayOf(
        360, 384, 392, 400, 410, 411, 480, 533, 592,
        600, 640, 662, 720, 768, 800, 811, 820, 900, 960, 961, 1024, 1280
    ) //常见dp列表
    private var fixedThreadPool //线程池，用于生成XML文件
            : ExecutorService? = null
    private const val size_thread = 5 //线程池大小
    private var dbFactory: DocumentBuilderFactory? = null
    private var db: DocumentBuilder? = null
    private var document: Document? = null
    private const val root = "./app/src/main/res/"
    @TargetApi(Build.VERSION_CODES.FROYO)
    @JvmStatic
    fun main(args: Array<String>) {

        try {
            dbFactory = DocumentBuilderFactory.newInstance()
            db = dbFactory?.newDocumentBuilder()
            //将给定 URI 的内容解析为一个 XML 文档,并返回Document对象
            //记得改成自己当前项目的路径
            document = db?.parse(root + "values/dimens.xml")


            //按文档顺序返回包含在文档中且具有给定标记名称的所有 Element 的 NodeList
            val dimenList = document?.getElementsByTagName("dimen")
            if (dimenList!!.length == 0) return
            val list: MutableList<Dimen> = ArrayList()
            for (i in 0 until dimenList.length) {
                //获取第i个book结点
                val node = dimenList.item(i)
                //获取第i个dimen的所有属性
                val namedNodeMap = node.attributes
                //获取已知名为name的属性值
                val atrName = namedNodeMap.getNamedItem("name").textContent
                val value = node.textContent
                println("+++atrName++++++++++++++++++++$atrName")
                println("+++++++++++++value++++++++++$value")
                list.add(Dimen(atrName, value))
            }
            fixedThreadPool = Executors.newFixedThreadPool(size_thread)
            for (i in dps.indices) {
                val xmlThread = XMLThread(i, list)
                fixedThreadPool?.execute(xmlThread) //线程启动执行
            }
        } catch (e: SAXException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ParserConfigurationException) {
            e.printStackTrace()
        }
    }

    private fun generateXMl(list: List<Dimen>, index: Int, pathDir: String, fileName: String) {
        try {
            val diectoryFile = File(pathDir)
            if (!diectoryFile.exists()) {
                diectoryFile.mkdirs()
            }
            val file = File(pathDir + fileName)
            if (file.exists()) {
                file.delete()
            }
            val fileWriter = FileWriter(file)
            fileWriter.write(HEAD)
            fileWriter.write(START_TAG)
            val size = list.size
            var atrName: String
            var value: String
            for (i in 0 until size) {
                atrName = list[i].atrName
                value = list[i].value
                val output = """	<dimen name="$atrName">${
                    roundString(
                        java.lang.Float.valueOf(
                            value.substring(
                                0,
                                value.length - 2
                            )
                        ), index
                    )
                }${value.substring(value.length - 2)}</dimen>
"""
                fileWriter.write(output)
            }
            fileWriter.write(END_TAG)
            fileWriter.flush()
            fileWriter.close()
            println("写入成功")
        } catch (e: IOException) {
            e.printStackTrace()
            println("写入失败")
        }
    }

    //精确到小数点后2位,并且四舍五入(因为有SW1280dp,基准是160dp，1dp=1px,
    // 如果精确到小数点后一位，四舍五入会有0.5dp误差，在sw1280dp中会有4PX误差，精确到小数点后2位，四舍五入，误差控制在1PX之内)
    private fun roundString(data: Float, index: Int): String {
        var result = ""
        val floatResult = data * dps[index] / DP_BASE
        val df = DecimalFormat("0.00")
        result = df.format(floatResult.toDouble())
        return result
    }

    private class XMLThread(index: Int, list: List<Dimen>) : Runnable {
        private var index = 0
        private val list: List<Dimen>
        override fun run() {
            //记得改成自己当前项目的路径
            generateXMl(list, index, root + "values-sw" + dps[index] + "dp/", "dimens.xml")
        }

        init {
            this.index = index
            this.list = list
        }
    }

    private class Dimen(var atrName: String, var value: String)
}