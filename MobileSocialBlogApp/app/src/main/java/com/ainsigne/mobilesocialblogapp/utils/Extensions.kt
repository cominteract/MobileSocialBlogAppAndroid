package com.ainsigne.mobilesocialblogapp.utils

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

@SuppressLint("ConstantLocale")
val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
var cal = Calendar.getInstance()
fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun HashMap<String,String>.URLQuery() : String
{
    var values = ""

    for(key in this.keys)
    {
        values += "$key=${this[key]}&"
    }
    return values.removeSuffix("&")
}

fun Date.fromNow() : String
{

    cal.time = this
    var year = Calendar.getInstance().get(Calendar.YEAR) - cal.get(Calendar.YEAR)
    var month = Calendar.getInstance().get(Calendar.MONTH) - cal.get(Calendar.MONTH)
    if(month < 0)
        month += 12

    var day = Calendar.getInstance().get(Calendar.DAY_OF_YEAR) - cal.get(Calendar.DAY_OF_YEAR)
    if(day < 0)
        day += 365
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) - cal.get(Calendar.HOUR_OF_DAY)
    val minute = Calendar.getInstance().get(Calendar.MINUTE) - cal.get(Calendar.MINUTE)
    //cal.set(Calendar.DAY_OF_MONTH, currentDay - 2)

    Log.d(" Current Day "," Current Day $day")
    Log.d(" Current Month "," Current Month $month")
    Log.d(" Current Year "," Current Year $year")

    return if(month > 1 && day >= 30 && month < 12)
        "$month mos"
    else if(year > 0)
        "$year y"
    else if(month == 1 && day >= 30)
        "$month mo"
    else if(day > 0 )
        "$day d"
    else if(hour > 0 )
        "$hour h"
    else "$minute m"
}

fun Date.timeFromNow() : Int
{

    cal.time = this
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) - cal.get(Calendar.HOUR_OF_DAY)
    val day = Calendar.getInstance().get(Calendar.DAY_OF_YEAR) - cal.get(Calendar.DAY_OF_YEAR)
    if(day > 0)
        return day
    else if(hour > -1)
        return hour
    else return -1
}

fun Date.hoursFromNow() : Int
{

    cal.time = this
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) - cal.get(Calendar.HOUR_OF_DAY)
    return hour
}

fun Date.daysFromNow() : Int
{
    cal.time = this
    val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) - cal.get(Calendar.DAY_OF_MONTH)
    return day
}

fun Date.monthFromNow() : Int
{
    cal.time = this
    val month = Calendar.getInstance().get(Calendar.MONTH) - cal.get(Calendar.MONTH)
    return month
}



fun Date.yearFromNow() : Int
{
    cal.time = this
    val year = Calendar.getInstance().get(Calendar.YEAR) - cal.get(Calendar.YEAR)
    return year
}

fun Double.toDateFromUTC() : String
{
    return try {
        val updatedate = Date(this.toLong() * 1000)
        val format = SimpleDateFormat("dd-MM-yyyy HH mm a")
        val dateStr = format.format(updatedate)
        val newdate = format.parse(dateStr)
        val finalDate = formatter.format(newdate)
        return finalDate.toDate().fromNow()
    } catch (e: Exception) {
        e.localizedMessage
    }
}

fun Long.toDateFromUTC() : String
{
    try {
        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        cal.timeInMillis = this

        formatter.timeZone = TimeZone.getDefault()
        var date = formatter.format(formatter.parse(cal.timeInMillis.toString()))
        return date
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}

fun String.toDateFromUTC(dateFormat: String = "yyyy-MM-dd HH:mm:ss", timeZone: TimeZone = TimeZone.getTimeZone("UTC")): Date {
    val parser = SimpleDateFormat(dateFormat, Locale.getDefault())
    parser.timeZone = timeZone
    return parser.parse(this)
}

fun Date.formatTo(dateFormat: String, timeZone: TimeZone = TimeZone.getDefault()): String {
    val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
    formatter.timeZone = timeZone
    return formatter.format(this)
}

fun Date.toStringFormat() : String
{
    return formatter.format(this)
}

fun Date.toStringFull() : String
{
    return formatter.format("yyyy-MM-dd-HH:mm:ss").format(this)
}

fun Date.toStringYear() : String
{
    return formatter.format("yyy").format(this)
}

fun Date.toStringMonth() : String
{
    return formatter.format("yyyy-MM").format(this)
}

fun Date.toStringDay() : String
{
    return formatter.format("yyyy-MMM-dd").format(this)
}

fun Long.toDate() : Date
{
    return formatter.parse(formatter.format(this))
}

fun Long.toDateString() : String
{
    return formatter.format(this)
}

fun Double.toStringNoExp() : String
{

    if (this.isNaN() || this.isInfinite())
        return this.toString()

    // Pre Java 8, a value of 0 would yield "0.0" below
    if (this == 0.0)
        return "0";
    return BigDecimal(this.toString()).stripTrailingZeros().toPlainString();
}

fun Double.toDate() : Date
{
    return formatter.parse(formatter.format(this))
}

fun Double.toDateString() : String
{
    return formatter.format(this)
}

fun Double.roundTo(value : Int) : String
{
    return String.format("%.${value}f", this)
}
fun Double.roundToThis(value : Int) : Double
{
    return String.format("%.${value}f", this).toDouble()
}

fun String.toDate() : Date
{
    return formatter.parse(this)
}

fun String.oneHourPassed() : Boolean
{
    return formatter.parse(this).timeFromNow() > -1
}

fun String.hoursFromNow() : Int
{
    return formatter.parse(this).timeFromNow()
}

fun String.toMap() : JsonObject
{
    return Json.nonstrict.parseJson(this).jsonObject
}

