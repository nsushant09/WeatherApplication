package com.neupanesushant.weather.domain.extras

import com.neupanesushant.weather.R
import java.util.concurrent.TimeUnit

object Utils {

    fun getWeatherIcon(description : String) : Int {
        if(description.equals("01d",true) || description.equals("01n",true)){
            return R.drawable.ic_clearsky
        }else if(description.equals("02d", true) || description.equals("02n",true) ){
            return R.drawable.ic_fewclouds
        }else if (description.equals("03d", true) || description.equals("03n",true)){
            return R.drawable.ic_scatteredcloud
        }else if(description.equals("04d", true) || description.equals("04n",true)){
            return R.drawable.ic_broken_clouds
        }else if(description.equals("09d", true) || description.equals("09n",true)){
            return R.drawable.ic_showerrain
        }else if(description.equals("10d", true) || description.equals("10n",true)){
            return R.drawable.ic_rain
        }else if(description.equals("11d", true) || description.equals("11n",true)){
            return R.drawable.ic_thunderstorm
        }else if(description.equals("13d", true) || description.equals("13n",true)){
            return R.drawable.ic_snow
        }else{
            return R.drawable.ic_mist
        }
    }

    fun getWeatherImage(description : String) : String{

        when(description){
            "01d" -> return "https://images.unsplash.com/photo-1530530824905-661c2bb787f6?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8NXx8Y2xlYXIlMjBza3l8ZW58MHx8MHx8&auto=format&fit=crop&w=800&q=60"
            "01n" -> return "https://images.unsplash.com/photo-1516339901601-2e1b62dc0c45?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8N3x8bmlnaHQlMjBubyUyMGNvcHlyaWdodHxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=800&q=60"
            "02d" -> return "https://images.unsplash.com/photo-1558089858-f49426a78c5a?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxjb2xsZWN0aW9uLXBhZ2V8OXw5ODc3NDQ0fHxlbnwwfHx8fA%3D%3D&auto=format&fit=crop&w=800&q=60"
            "02n" -> return "https://images.unsplash.com/photo-1593977379931-ce8b3e44c7dd?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8NXx8bmlnaHQlMjBjbG91ZHN8ZW58MHx8MHx8&auto=format&fit=crop&w=800&q=60"
            "03d" -> return "https://cdn.pixabay.com/photo/2013/04/09/17/08/sunset-102234__480.jpg"
            "03n" -> return "https://cdn.pixabay.com/photo/2018/05/04/12/43/monolithic-part-of-the-waters-3373906__480.jpg"
            "04d" -> return "https://images.unsplash.com/photo-1533736405784-798e2e103a3f?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8N3x8YnJva2VuJTIwY2xvdWRzfGVufDB8fDB8fA%3D%3D&auto=format&fit=crop&w=800&q=60"
            "04n" -> return "https://images.unsplash.com/photo-1558424774-86401550d687?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8Mnx8bmlnaHQlMjBjbG91ZHN8ZW58MHx8MHx8&auto=format&fit=crop&w=800&q=60"
            "09d" -> return "https://cdn.pixabay.com/photo/2014/04/05/11/39/rain-316579__340.jpg"
            "09n" -> return "https://cdn.pixabay.com/photo/2015/03/11/13/54/rain-668694__480.jpg"
            "10d" -> return "https://cdn.pixabay.com/photo/2014/04/05/11/39/rain-316579__340.jpg"
            "10n" -> return "https://cdn.pixabay.com/photo/2015/03/11/13/54/rain-668694__480.jpg"
            "11d" -> return "https://cdn.pixabay.com/photo/2019/12/22/22/00/lightning-4713379__480.jpg"
            "11n" -> return "https://cdn.pixabay.com/photo/2015/09/23/08/16/thunder-953118__340.jpg"
            "13d" -> return "https://images.unsplash.com/photo-1551234250-d88208c2ce14?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTB8fHRodW5kZXJzdG9ybXxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=800&q=60"
            "13n" -> return "https://images.unsplash.com/photo-1583459094467-e0db130c0dea?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8N3x8dGh1bmRlcnN0b3JtfGVufDB8fDB8fA%3D%3D&auto=format&fit=crop&w=800&q=60"
            else -> return "https://cdn.pixabay.com/photo/2015/11/06/09/53/trees-1025783__340.jpg"
        }
    }


    fun convertKelvinToCelsius(tempInKelvin : Double) : String{
        return String.format("%02.1f°", tempInKelvin - 273.15)
    }

    fun convertTimeToLocalTime(time: Long, offset: Long): String {
        val finalTime = time + offset
        val hours = TimeUnit.SECONDS.toHours(finalTime) % 24
        val minutes = TimeUnit.SECONDS.toMinutes(finalTime) % 60

        return String.format("%02d:%02d", hours, minutes)

    }

    fun convertTimeToDay(time : Long) : String{
        var day : Int = TimeUnit.SECONDS.toDays(time).toInt()
        day %= 7
        return when(day){
            1 -> "Saturday"
            2 -> "Sunday"
            3 -> "Monday"
            4 -> "Tuesday"
            5 -> "Wednesday"
            6 -> "Thursday"
            0 -> "Friday"
            else -> "Error"
        }
    }
}