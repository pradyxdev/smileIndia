package com.app.ulife.creatoron.models.networkManage.downline

data class GetDownlineReq(
    val apiname: String,
    val obj: Obj
)

data class Obj(
    val direction: String,
    val from: String,
    val status: String,
    val to: String,
    val userid: String
)