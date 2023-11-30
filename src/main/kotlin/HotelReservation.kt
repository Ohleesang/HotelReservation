import java.lang.NumberFormatException
import java.text.SimpleDateFormat
import java.util.Date

/**
 *
 * 호텔 예약 프로그램 기능들(Methods)
 *
 * 1. 방예약,
 * 2. 예약 목록 출력,
 * 3. 예약 목력(정렬) 출력,
 * 4. 시스템 종료,
 * 5. 금액 입금-출금 내역 목록 출력
 * 6. 예약 변경/취소
 *
 * 프로그램 기능에 대한 변수들(Property)
 * 성함
 * 방번호(100~999)
 * 체크인 날짜
 * 체크 아웃 날짜 > 체크인 < 체크아웃
 *
 */

class HotelReservation{

    // 입력에 대한 변수
    var input :String? =""

    // 호텔 관련 프로퍼티
    var name : String? =""
    var roomNum : Int = -1
    var checkInDate : Date = Date()
    var checkOutDate : Date = Date()
    var fee = 0//호텔 예약비


    // 소비자?에 대한 프로퍼티
    var money = 0
    //앱 실행
    fun executeApp(){
        while(true) {//4.시스템 종료까지 계산 반복
            println("호텔예약 프로그램 입니다.")
            println("[메뉴]")
            println("1. 방예약, 2. 예약 목록 출력, 3. 예약 목력(정렬) 출력, 4. 시스템 종료, 5. 금액 입금-출금 내역 목록 출력 6. 예약 변경/취소")
            input = readLine()
            when(input){
                "1" -> reservationRoom()
                "2" -> reservationList()
                "3" -> sortedReservationList()
                "4" -> quitApp()
                "5" -> billList()
                "6" -> reservationChange()
            }
        }
    }

    //1.방예약 프로그램
    fun reservationRoom(){

        //성함 입력
        while(true) {
            println("예약자분의 성함을 입력해주세요")
            input = readLine()
            if(input=="") continue
            this.name = input.toString()
            break
        }

        //방번호 입력
        var roomNum :Int?
        while(true) {
            //올바른 정수값이 들어오지않으면 예외처리
            try {
                println("예약할 방번호를 입력해주세요")
                input = readLine()
                roomNum = input?.toInt()!!
                if (roomNum >= 100 && roomNum <= 999){
                    this.roomNum =roomNum
                    break
                }
                else println("올바르지 않은 방번호 입니다. 방번호는 100~999 영역이내 입니다.")
            }catch(e:NumberFormatException){
                println("올바르지 않은 방번호 입니다. 방번호는 100~999 영역이내 입니다.")
            }
        }

        //체크인 날짜 입력
        println("체크인 날짜를 입력해주세요 표기형식. 20231130")

        while(true) {
            try {
                input = readLine()
                //date 클래스에 맞게 파싱한다(문자열 -> Date 객체)
                //데이터의 형식을 이해하고, 해당 형식에 맞게 데이터를 처리하는 과정
                val dataFormat = SimpleDateFormat("yyyyMMdd")
                this.checkInDate = dataFormat.parse(input)
                break
            } catch (e: Exception) {
            }
        }

        //체크아웃 날짜 입력
        println("체크아웃 날짜를 입력해주세요 표기형식. 20231204")

        while(true) {
            try {
                input = readLine()
                //date 클래스에 맞게 파싱한다(문자열 -> Date 객체)
                //데이터의 형식을 이해하고, 해당 형식에 맞게 데이터를 처리하는 과정
                val dataFormat = SimpleDateFormat("yyyyMMdd")
                val date = dataFormat.parse(input)

                //체크인 날짜 랑 같거나 이전이면 안된다!
                if(date.before(this.checkInDate)||date ==this.checkInDate){
                    println("체크인 날짜보다 이전이거나 같을수 없습니다.")
                    continue
                }
                else{
                    this.checkOutDate=date
                    break
                }
            } catch (e: Exception) {
            }
        }


        println("호텔예약이 완료되었습니다.")


        //이때 임의의 금액을 지급해주고 랜덤으로 호텔 예약비로 빠져나가도록
        consumerSetMoneyRand()
        setFeeRand()
        //뺀값을 입력후 출력
        this.money -= this.fee
        println("예약자분의 현재 돈  : ${this.money}")
        println()

    }
    //2.예약목록 출력
    fun reservationList(){

    }
    //3.예약목록 (정렬) 출력
    fun sortedReservationList(){

    }
    //4.시스템 종료
    fun quitApp(){

    }
    //5.금액 입금-출금 내역 목록 출력
    fun billList(){

    }
    //6.예약 변경/취소
    fun reservationChange(){

    }

    //소비자? 에 임의의 머니를 지급
    fun consumerSetMoneyRand(){
//        this.money= (100000..1000000).random()
        var range = (100000..1000000 step 1000)// 천원 단위로 랜덤값 주고싶어서..
        this.money = (range.first..range.last).random()
        println("예약자분의 현재 돈  : ${this.money}")
    }

    //호텔 예약비 임의로 지정
    fun setFeeRand(){
        var range = (100000..1000000 step 10000)
        this.fee = (range.first..range.last).random()
        println("호텔 예약비  : ${this.fee}")
    }
}

fun main(){
    var a = HotelReservation()
    var input : String?
    while(true){
        a.executeApp()
    }
}