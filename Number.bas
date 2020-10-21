B4A=true
Group=Default Group
ModulesStructureVersion=1
Type=StaticCode
Version=6.5
@EndOfDesignText@
'Number V 1.0
'Code module


Sub Process_Globals
	Public TempFormat, TempFormat1 As String
	Private NumTemp As Double
	Private Explen, LastSep As Int
End Sub


'Format3 operates in a similar manner to NumberFormat2 with addittional parameters to provide specified separators and limit the number of characters.
'
'Parameters:
'Num:  the number to be formated
'MinInt:  The minimum number of intergers
'MaxDec:  The maximum number of decimal places
'MinDec:  The minimum number of decimal places
'DecSep:  The charater used to as the decimal separator.  EG: "," as DecSep.  Pi = 3,14159			Do Not use # or ? as separators
'FstThouSep:  The first thousand separator.  EG: using the "." as FstThouSep with a space as the other separators.   = 4 358.752,99
'OtherThouSep:  The other thousand separator.  May be the same as FstThouSep or different to suit the desired format convention as above.
'ExpType:  Expression or exponent type. Formats the number in to one of three formats.  
		'ExpType = 0:	Floating with MaxStrLen = 12.	EG: 4358752.537654 = 435.875254E4
		'ExpType = 1:	Scientific format.				EG: 24358752.537654 = 2.4358752537654E7
		'ExpType = 2:	Engineering format.				EG: 43587525.37654 = 43.58752537654E6
'MaxStrLen: Sets the maximum number of characters in the final formated string.  
'If the format length exceeds MaxStrLen it will be reformated with reduced decimal places

Sub Format3(Num As Double,MinInt As Int,MaxDec As Int,MinDec As Int,DecSep As String,FstThouSep As String,OtherThouSep As String,ExpType As Int, MaxStrLen As Int) As String
	NumTemp = Num
	Explen = 0
	
	Select ExpType
		Case 0			'Floating number possible Exponent notation
			TempFormat = FloatNum(Num, MinInt, MaxDec, MinDec, DecSep, FstThouSep, OtherThouSep, ExpType, MaxStrLen)
										
		Case 1			'Use Scientific Notation.  1 interger
			TempFormat = ScientificNum(Num ,MinInt ,MaxDec ,MinDec ,DecSep ,FstThouSep ,OtherThouSep ,ExpType , MaxStrLen )
			
		Case 2			'Use Engineering notation, Exponent in multiples of 3
			TempFormat = EngineeringNum(Num ,MinInt ,MaxDec ,MinDec ,DecSep ,FstThouSep ,OtherThouSep ,ExpType , MaxStrLen ) 
			
	End Select
	'Change separators
	'TempFormat = TempFormat1
	TempFormat = TempFormat.Replace(".", "?")
	TempFormat = TempFormat.Replace(",", "#")
	If TempFormat.Contains("#") Then
		LastSep = TempFormat.LastIndexOf("#")
		TempFormat = TempFormat.SubString2(0,LastSep)&FstThouSep&TempFormat.SubString2(LastSep+1,TempFormat.Length)
	End If
	TempFormat = TempFormat.Replace("?", DecSep)
	TempFormat = TempFormat.Replace("#", OtherThouSep)
	Return TempFormat
End Sub


Sub FloatNum(Num As Double,MinInt As Int,MaxDec As Int,MinDec As Int,DecSep As String,FstThouSep As String,OtherThouSep As String,ExpType As Int, MaxStrLen As Int) As String
	'If number to big to display convert to exponetial display
	TempFormat = NumberFormat2(Num, MinInt, MaxDec,MinDec,True)
	If TempFormat.Length > MaxStrLen Then
		If Abs(NumTemp) > 10 Then  '99999
			Do While TempFormat.Length > MaxStrLen And Abs(NumTemp) > 10
				NumTemp = NumTemp/10
				Explen = Explen + 1
				TempFormat = NumberFormat2(NumTemp, MinInt, MaxDec,MinDec,True)
				If Explen <> 0 Then TempFormat = TempFormat&"E"&Explen
			Loop
			Do While TempFormat.Length > MaxStrLen And MaxDec > MinDec
				MaxDec = MaxDec - 1
				TempFormat = NumberFormat2(NumTemp, MinInt, MaxDec,MinDec,True)
				If Explen <> 0 Then TempFormat = TempFormat&"E"&Explen
			Loop
			'If number is small, convert to exponential display
		Else If Abs(NumTemp) < 1 And NumTemp <> 0 Then
			Do While TempFormat.Length > MaxStrLen And Abs(NumTemp) < 1
				NumTemp = NumTemp * 10
				Explen = Explen - 1
				TempFormat = NumberFormat2(NumTemp, MinInt, MaxDec,MinDec,True)
				If Explen <> 0 Then TempFormat = TempFormat&"E"&Explen
			Loop
			Do While TempFormat.Length > MaxStrLen And MaxDec > MinDec
				MaxDec = MaxDec - 1
				TempFormat = NumberFormat2(NumTemp, MinInt, MaxDec,MinDec,True)
				If Explen <> 0 Then TempFormat = TempFormat&"E"&Explen
			Loop
		Else
			'If number is greater than 1 and less than 10 trim decimal places to reduce string length
			Do While TempFormat.Length > MaxStrLen And MaxDec > MinDec
				MaxDec = MaxDec - 1
				TempFormat = NumberFormat2(NumTemp, MinInt, MaxDec,MinDec,True)
			Loop
		End If
	End If
	Return TempFormat
End Sub

Sub ScientificNum(Num As Double,MinInt As Int,MaxDec As Int,MinDec As Int,DecSep As String,FstThouSep As String,OtherThouSep As String,ExpType As Int, MaxStrLen As Int) As String
	Do While Abs(NumTemp) < 1
		NumTemp = NumTemp * 10
		Explen = Explen - 1
	Loop
	Do While Abs(NumTemp) > 10
		NumTemp = NumTemp/10
		Explen = Explen + 1
	Loop
	TempFormat = NumberFormat2(NumTemp, MinInt, MaxDec,MinDec,True)
	If Explen <> 0 Then TempFormat = TempFormat&"E"&Explen
	Do While TempFormat.Length > MaxStrLen And MaxDec > MinDec
		MaxDec = MaxDec - 1
		TempFormat = NumberFormat2(NumTemp, MinInt, MaxDec,MinDec,True)
		If Explen <> 0 Then TempFormat = TempFormat&"E"&Explen
	Loop
	Return TempFormat
End Sub

Sub EngineeringNum(Num As Double,MinInt As Int,MaxDec As Int,MinDec As Int,DecSep As String,FstThouSep As String,OtherThouSep As String,ExpType As Int, MaxStrLen As Int) As String
	Do While Abs(NumTemp) < 1
		NumTemp = NumTemp * 1000
		Explen = Explen - 3
	Loop
	Do While Abs(NumTemp) > 1000
		NumTemp = NumTemp/1000
		Explen = Explen + 3
	Loop
	TempFormat = NumberFormat2(NumTemp, MinInt, MaxDec,MinDec,True)
	If Explen <> 0 Then TempFormat = TempFormat&"E"&Explen
	Do While TempFormat.Length > MaxStrLen And MaxDec > MinDec
		MaxDec = MaxDec - 1
		TempFormat = NumberFormat2(NumTemp, MinInt, MaxDec,MinDec,True)
		If Explen <> 0 Then TempFormat = TempFormat&"E"&Explen
	Loop
	Return TempFormat
End Sub