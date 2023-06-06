package com.devdcrq.usastateparks.util

import kotlin.random.Random

object Constants {

    const val API_KEY = ""
    const val BASE_URL = "https://developer.nps.gov/api/v1/"
    const val PARKS_RESULT_LIMIT_PARAM = 496
    const val MAP_API_KEY = ""
    const val SEARCH_TIME_DELAY = 500L

    val stateNames = arrayListOf("Alabama","Alaska","Arizona","Arkansas","California","Colorado",
        "Connecticut","Delaware","Florida","Georgia","Hawaii","Idaho","Illinois","Indiana","Iowa",
        "Kansas","Kentucky","Louisiana","Maine","Maryland","Massachusetts","Michigan","Minnesota",
        "Mississippi","Missouri","Montana","Nebraska","Nevada","New Hampshire","New Jersey",
        "New Mexico","New York","North Carolina","North Dakota","Ohio","Oklahoma","Oregon",
        "Pennsylvania","Rhode Island","South Carolina","South Dakota","Tennessee","Texas","Utah",
        "Vermont","Virginia","Washington","West Virginia","Wisconsin","Wyoming")
    val stateCodes = arrayListOf("AL","AK","AZ","AR","CA","CO","CT","DE","FL","GA","HI","ID","IL",
        "IN","IA","KS","KY","LA","ME","MD","MA","MI","MN","MS","MO","MT","NE","NV","NH","NJ","NM",
        "NY","NC","ND","OH","OK","OR","PA","RI","SC","SD","TN","TX","UT","VT","VA","WA","WV","WI","WY")

    fun getRandomIndex(): Int {
        return Random.nextInt(0, stateCodes.size)
    }
}