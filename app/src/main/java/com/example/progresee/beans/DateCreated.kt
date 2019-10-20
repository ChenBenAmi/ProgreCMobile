package com.example.progresee.beans
import com.google.gson.annotations.SerializedName



data class DateCreated (

	@SerializedName("seconds") val seconds : Int,
	@SerializedName("nanos") val nanos : Int
)