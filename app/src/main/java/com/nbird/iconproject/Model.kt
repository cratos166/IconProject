package com.nbird.iconproject

public class Model{
    lateinit var imageUrl:String
    lateinit var name:String


    constructor(imageUrl: String,name:String) {
        this.imageUrl = imageUrl
        this.name = name
    }

    constructor()
}