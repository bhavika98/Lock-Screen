package com.app.incroyable.lockscreen.storage

import com.app.incroyable.lockscreen.R
import com.app.incroyable.lockscreen.enums.UnlockBar
import com.app.incroyable.lockscreen.model.QuestionData
import com.app.incroyable.lockscreen.model.ThemeData

fun themeStorage(): ArrayList<ThemeData> {

    val themeList = ArrayList<ThemeData>()

    themeList.add(ThemeData().apply {
        position = 0
        thumb = R.drawable.theme_1
        layout = R.layout.layout_lockview_1
        unlockBar = UnlockBar.BOTTOM
        dayFormat = "EEEE"
        dateFormat = "MMM, dd"
    })

    themeList.add(ThemeData().apply {
        position = 1
        thumb = R.drawable.theme_2
        layout = R.layout.layout_lockview_2
        unlockBar = UnlockBar.BOTTOM_UP
        dayFormat = "EEEE"
        dateFormat = "MMMM, dd"
    })

    themeList.add(ThemeData().apply {
        position = 2
        thumb = R.drawable.theme_3
        layout = R.layout.layout_lockview_3
        unlockBar = UnlockBar.BOTTOM
        dayFormat = "EEEE"
        dateFormat = "dd MMM"
    })

    themeList.add(ThemeData().apply {
        position = 3
        thumb = R.drawable.theme_4
        layout = R.layout.layout_lockview_4
        unlockBar = UnlockBar.BOTTOM
        dayFormat = "EEEE"
        dateFormat = "MMMM, dd"
    })

    themeList.add(ThemeData().apply {
        position = 4
        thumb = R.drawable.theme_5
        layout = R.layout.layout_lockview_5
        unlockBar = UnlockBar.BOTTOM
        dayFormat = "EEEE"
        dateFormat = "MMM, dd"
    })

    themeList.add(ThemeData().apply {
        position = 5
        thumb = R.drawable.theme_6
        layout = R.layout.layout_lockview_6
        unlockBar = UnlockBar.BOTTOM
        dayFormat = "EEEE"
        dateFormat = "MMM, dd"
    })

    themeList.add(ThemeData().apply {
        position = 6
        thumb = R.drawable.theme_7
        layout = R.layout.layout_lockview_7
        unlockBar = UnlockBar.BOTTOM
        dayFormat = "EEEE"
        dateFormat = "MMM, dd"
    })

    themeList.add(ThemeData().apply {
        position = 7
        thumb = R.drawable.theme_8
        layout = R.layout.layout_lockview_8
        unlockBar = UnlockBar.CENTER
        dayFormat = "EEEE"
        dateFormat = "dd\nMMM"
    })

    themeList.add(ThemeData().apply {
        position = 8
        thumb = R.drawable.theme_9
        layout = R.layout.layout_lockview_9
        unlockBar = UnlockBar.BOTTOM
        dayFormat = "EEE, "
        dateFormat = "dd MMM"
    })

    themeList.add(ThemeData().apply {
        position = 9
        thumb = R.drawable.theme_10
        layout = R.layout.layout_lockview_10
        unlockBar = UnlockBar.TOP
        dayFormat = "EEEE"
        dateFormat = "MMM, dd"
    })

    themeList.add(ThemeData().apply {
        position = 10
        thumb = R.drawable.theme_11
        layout = R.layout.layout_lockview_11
        unlockBar = UnlockBar.CENTER
        dayFormat = "EEEE"
        dateFormat = "MMM, dd yyyy"
    })

    themeList.add(ThemeData().apply {
        position = 11
        thumb = R.drawable.theme_12
        layout = R.layout.layout_lockview_12
        unlockBar = UnlockBar.TOP
        dayFormat = "EEEE"
        dateFormat = "MMM, dd"
    })

    themeList.add(ThemeData().apply {
        position = 12
        thumb = R.drawable.theme_13
        layout = R.layout.layout_lockview_13
        unlockBar = UnlockBar.BOTTOM
        dayFormat = "EEEE, "
        dateFormat = "dd MMM"
    })

    themeList.add(ThemeData().apply {
        position = 13
        thumb = R.drawable.theme_14
        layout = R.layout.layout_lockview_14
        unlockBar = UnlockBar.BOTTOM
        dayFormat = "EEEE"
        dateFormat = "MMM, dd"
    })

    themeList.add(ThemeData().apply {
        position = 14
        thumb = R.drawable.theme_15
        layout = R.layout.layout_lockview_15
        unlockBar = UnlockBar.BOTTOM
        dayFormat = "EEEE"
        dateFormat = "MMM, dd"
    })

    return themeList
}

fun questionStorage(): ArrayList<QuestionData> {

    val questionList = ArrayList<QuestionData>()

    questionList.add(
        QuestionData(
            position = 1,
            quality = 1,
            question = "What was your childhood nickname?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 2,
            quality = 1,
            question = "In what city did you meet your spouse/significant other?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 3,
            quality = 1,
            question = "What is the name of your favorite childhood friend?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 4,
            quality = 1,
            question = "What street did you live on in third grade?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 5,
            quality = 1,
            question = "What is your oldest sibling’s birthday month and year?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 6,
            quality = 1,
            question = "What is the middle name of your youngest child?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 7,
            quality = 1,
            question = "What is your oldest sibling's middle name?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 8,
            quality = 1,
            question = "What school did you attend for sixth grade?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 9,
            quality = 1,
            question = "What was your childhood phone number including area code?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 10,
            quality = 1,
            question = "What is your oldest cousin's first and last name?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 11,
            quality = 1,
            question = "What was the name of your first stuffed animal?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 12,
            quality = 1,
            question = "In what city or town did your mother and father meet?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 13,
            quality = 1,
            question = "Where were you when you had your first kiss?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 14,
            quality = 1,
            question = "What is the first name of the boy or girl that you first kissed?",
            false
        )
    )

    questionList.add(
        QuestionData(
            position = 15,
            quality = 1,
            question = "What was the last name of your third grade teacher?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 16,
            quality = 1,
            question = "In what city does your nearest sibling live?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 17,
            quality = 1,
            question = "What is your youngest brother’s birthday month and year?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 18,
            quality = 1,
            question = "What is your maternal grandmother's maiden name?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 19,
            quality = 1,
            question = "In what city or town was your first job?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 20,
            quality = 1,
            question = "What is the name of the place your wedding reception was held?",
            false
        )
    )

    questionList.add(
        QuestionData(
            position = 21,
            quality = 1,
            question = "What is the name of a college you applied to but didn't attend?",
            false
        )
    )

    questionList.add(
        QuestionData(
            position = 22,
            quality = 1,
            question = "Where were you when you first heard about 9/11?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 23,
            quality = 2,
            question = "What was the name of your elementary / primary school?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 24,
            quality = 2,
            question = "What is the name of the company of your first job?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 25,
            quality = 2,
            question = "What was your favorite place to visit as a child?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 26,
            quality = 2,
            question = "What is your spouse's mother's maiden name?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 27,
            quality = 2,
            question = "What is the country of your ultimate dream vacation?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 28,
            quality = 2,
            question = "What is the name of your favorite childhood teacher?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 29,
            quality = 2,
            question = "To what city did you go on your honeymoon?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 30,
            quality = 2,
            question = "What time of the day were you born?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 31,
            quality = 2,
            question = "What was your dream job as a child?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 32,
            quality = 2,
            question = "What is the street number of the house you grew up in?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 33,
            quality = 2,
            question = "What is the license plate (registration) of your dad's first car?",
            false
        )
    )

    questionList.add(
        QuestionData(
            position = 34,
            quality = 2,
            question = "Who was your childhood hero?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 35,
            quality = 2,
            question = "What was the first concert you attended?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 36,
            quality = 2,
            question = "What are the last 5 digits of your credit card?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 37,
            quality = 2,
            question = "What are the last 5 of your Social Security number?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 38,
            quality = 2,
            question = "What is your current car registration number?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 39,
            quality = 2,
            question = "What are the last 5 digits of your driver's license number?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 40,
            quality = 2,
            question = "What month and day is your anniversary?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 41,
            quality = 2,
            question = "What is your grandmother's first name?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 42,
            quality = 2,
            question = "What is your mother's middle name?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 43,
            quality = 2,
            question = "What is the last name of your favorite high school teacher?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 44,
            quality = 2,
            question = "What was the make and model of your first car?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 45,
            quality = 2,
            question = "Where did you vacation last year?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 46,
            quality = 2,
            question = "What is the name of your grandmother's dog?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 47,
            quality = 2,
            question = "What is the name, breed, and color of current pet?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 48,
            quality = 2,
            question = "What is your preferred musical genre?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 49,
            quality = 2,
            question = "In what city and country do you want to retire?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 50,
            quality = 2,
            question = "What is the name of the first undergraduate college you attended?",
            false
        )
    )

    questionList.add(
        QuestionData(
            position = 51,
            quality = 2,
            question = "What was your high school mascot?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 52,
            quality = 2,
            question = "What year did you graduate from High School?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 53,
            quality = 2,
            question = "What is the name of the first school you attended?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 54,
            quality = 3,
            question = "What was your favorite sport in high school?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 55,
            quality = 3,
            question = "What is the name of the High School you graduated from?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 56,
            quality = 3,
            question = "What is your pet's name?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 57,
            quality = 3,
            question = "In what year was your father born?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 58,
            quality = 3,
            question = "In what year was your mother born?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 59,
            quality = 3,
            question = "What is your mother’s (father's) first name?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 60,
            quality = 3,
            question = "What is your mother's maiden name?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 61,
            quality = 3,
            question = "What was the color of your first car?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 62,
            quality = 3,
            question = "What is your father's middle name?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 63,
            quality = 3,
            question = "In what county where you born?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 64,
            quality = 3,
            question = "How many bones have you broken?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 65,
            quality = 3,
            question = "What is the first and last name of your favorite college professor?",
            false
        )
    )

    questionList.add(
        QuestionData(
            position = 66,
            quality = 3,
            question = "On which wrist do you wear your watch?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 67,
            quality = 3,
            question = "What is the color of your eyes?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 68,
            quality = 3,
            question = "What is the title and artist of your favorite song?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 69,
            quality = 3,
            question = "What is the title and author of your favorite book?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 70,
            quality = 3,
            question = "What is the name, breed, and color of your favorite pet?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 71,
            quality = 3,
            question = "What is your favorite animal?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 72,
            quality = 3,
            question = "What was the last name of your favorite teacher?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 73,
            quality = 3,
            question = "What is your favorite team?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 74,
            quality = 3,
            question = "What is your favorite movie?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 75,
            quality = 3,
            question = "What is your favorite teacher's nickname?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 76,
            quality = 3,
            question = "What is your favorite TV program?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 77,
            quality = 3,
            question = "What is your least favorite nickname?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 78,
            quality = 3,
            question = "What is your favorite sport?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 79,
            quality = 3,
            question = "What is the name of your hometown?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 80,
            quality = 3,
            question = "What is the color of your father’s eyes?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 81,
            quality = 3,
            question = "What is the color of your mother’s eyes?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 82,
            quality = 3,
            question = "What was the name of your first pet?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 83,
            quality = 3,
            question = "What sports team do you love to see lose?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 84,
            quality = 3,
            question = "In what city were you born?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 85,
            quality = 3,
            question = "What is the city, state/province, and year of your birth?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 86,
            quality = 3,
            question = "What is the name of your hometown newspaper?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 87,
            quality = 3,
            question = "What is your favorite color?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 88,
            quality = 3,
            question = "What was your hair color as a child?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 89,
            quality = 3,
            question = "What is your work address?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 90,
            quality = 3,
            question = "What is the street name your work or office is located on?", false
        )
    )

    questionList.add(
        QuestionData(
            position = 91,
            quality = 3,
            question = "What is your address, phone number?", false
        )
    )

    return questionList
}