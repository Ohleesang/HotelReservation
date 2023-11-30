import java.lang.NumberFormatException
import java.time.LocalDate

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
    var customerList = mutableListOf<Customer>()

//    var name : String? ="" // 고객
//    var roomNum : Int = -1 // 예약룸
//    var checkInDate : Date = Date() //체크인 날짜
//    var checkOutDate : Date = Date() //체크아웃 날짜
//    var fee = 0//호텔 예약비


    // 고객에 대한 프로퍼티
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

        //고객님 리스트 작성
        var customer = Customer()
        //성함 입력
        while(true) {
            println("예약자분의 성함을 입력해주세요")
            input = readLine()
            if(input=="") continue
            customer.name = input.toString()
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
                    customer.roomNum =roomNum
                    break
                }
                else println("올바르지 않은 방번호 입니다. 방번호는 100~999 영역이내 입니다.")
            }catch(e:NumberFormatException){
                println("올바르지 않은 방번호 입니다. 방번호는 100~999 영역이내 입니다.")
            }
        }


        //체크인&체크아웃
        var isCheck = false
        while(!isCheck) {

            //체크인 날짜 입력
            println("체크인 날짜를 입력해주세요 표기형식. 20231130")

            while (true) {
                try {
                    input = readLine()
//                    customer.checkInDate = LocalDate.of(
//                        input?.substring(0..3)?.toIntOrNull()!!,
//                        input?.substring(4..5)?.toIntOrNull()!!,
//                        input?.substring(6..7)?.toIntOrNull()!!
//                    )
                    //입력된 값을 파싱!
                    var parsedText = input?.substring(0..3)+"-"+
                    input?.substring(4..5)+"-"+
                    input?.substring(6..7)
                    customer.checkInDate = LocalDate.parse(parsedText)
                    break
                } catch (e: Exception) {
                    println("파싱이 이상해요")
                }
            }

            //체크아웃 날짜 입력
            println("체크아웃 날짜를 입력해주세요 표기형식. 20231204")

            while (true) {
                try {
                    input = readLine()

                    //체크인 날짜 랑 같거나 이전이면 안된다!
//                    customer.checkOutDate = LocalDate.of(
//                        input?.substring(0..3)?.toIntOrNull()!!,
//                        input?.substring(4..5)?.toIntOrNull()!!,
//                        input?.substring(6..7)?.toIntOrNull()!!
//                    )

                    var parsedText = input?.substring(0..3)+"-"+
                            input?.substring(4..5)+"-"+
                            input?.substring(6..7)
                    customer.checkOutDate = LocalDate.parse(parsedText)

                    if (customer.checkInDate.isBefore(customer.checkOutDate)) {
                        //예약이 가능한지 안하는지 판별
                        if (isReservaiton(customer)) isCheck = true
                        else println("날짜가 겹쳐있거나 이미 기간 내에 예약된 방입니다.")

                        break
                    } else {
                        println("체크인 날짜보다 이전이거나 같을수 없습니다.")
                        continue
                    }
                } catch (e: Exception) {
                }
            }
        }

        println("호텔예약이 완료되었습니다.")


        //이때 임의의 금액을 지급해주고 랜덤으로 호텔 예약비로 빠져나가도록
        customer.customerSetMoneyRand()
        setFeeRand(customer)

        //뺀값을 입력후 출력
        customer.money -= customer.fee
        println("예약자분의 현재 돈  : ${customer.money}")


        customerList.add(customer) //리스트에 고객님 정보 입력완료!
        println()

    }

    //예약이 가능한가요??
    fun isReservaiton(c :Customer):Boolean{
        // 날짜가 겹쳐있는지 확인

        //dataRange         ![!------] dataRange 의 min : a
        //listRange     [------!]!     listRange 의 max : b
        // a>b 이면 겹쳐지 않는것!

        //dataRange     [------!]!     dataRange 의 max : b
        //listRange         ![!------] listRange 의 min : a
        // b<a 이면 겹치지 않는것!
        //만약 min의 큰 값이 max의 작은값 보다 크다면 겹쳐있지 않다?

        var dateRange = c.checkInDate..c.checkOutDate
        for(li in customerList){
            var listDataRange = li.checkInDate..li.checkOutDate

            var min = maxOf(dateRange.start,listDataRange.start)
            var max = minOf(dateRange.endInclusive,listDataRange.endInclusive)

            var isRange = min > max
            if(!isRange){
                //겹쳐 있다면 방은 비어있는지 확인해야함
                if(li.roomNum==c.roomNum) return false //방이 비어있지 않으므로 false

                else return true // 방은 비어 있으니까 예약가능!
            }
            else return true // 겹쳐 있지 않으므로 true
        }
        return true
    }
    //2.예약목록 출력
    fun reservationList(){
        if(customerList.isEmpty()){
            println("예약된 손님들이 없습니다!")
            return
        }

        for(c in customerList){
            print((customerList.indexOf(c)+1).toString()+". ")//1.
            print("사용자: "+c.name+", ")//사용자: 고객님, 
            print("방번호: "+c.roomNum.toString()+"호, ")//방번호 : xxx호, 
            print("체크인: "+c.checkInDate.toString()+", ")//2023-xx-xx .
            println("체크아웃: "+c.checkOutDate.toString())//2023-xx-xx >>>라인넘김
        }
    }
    //3.예약목록 (정렬) 출력
    fun sortedReservationList(){
        if(customerList.isEmpty()){
            println("예약된 손님들이 없습니다!")
            return
        }
        var temp = customerList.sortedBy{it.checkInDate}.toMutableList()
        for(c in temp){
            print((temp.indexOf(c)+1).toString()+". ")//1.
            print("사용자: "+c.name+", ")//사용자: 고객님,
            print("방번호: "+c.roomNum.toString()+"호, ")//방번호 : xxx호,
            print("체크인: "+c.checkInDate.toString()+", ")//2023-xx-xx .
            println("체크아웃: "+c.checkOutDate.toString())//2023-xx-xx >>>라인넘김
        }
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

    //호텔 예약비 임의로 지정
    fun setFeeRand(customer :Customer){
        var range = (100000..1000000 step 10000)
        customer.fee = (range.first..range.last).random()
        println("호텔 예약비  : ${customer.fee}")
    }
}

//고객 클래스
class Customer{
    var name : String? ="" // 고객
    var roomNum : Int = -1 // 예약룸
    var checkInDate : LocalDate = LocalDate.of(2000,1,1)  //체크인 날짜
    var checkOutDate : LocalDate =LocalDate.of(2000,1,1) //체크아웃 날짜
    var fee = 0//호텔 예약비
    var money = 0 // 고객 지갑..


    //고객에 임의의 머니를 지급
    fun customerSetMoneyRand(){
//        this.money= (100000..1000000).random()
        var range = (100000..1000000 step 1000)// 천원 단위로 랜덤값 주고싶어서..
        this.money = (range.first..range.last).random()
        println("예약자분의 현재 돈  : ${this.money}")
    }

}

fun main(){
    var a = HotelReservation()
    var input : String?
    while(true){
        a.executeApp()
    }
}