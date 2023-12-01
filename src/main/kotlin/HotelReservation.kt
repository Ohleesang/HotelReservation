import java.lang.NumberFormatException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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

        setNameCustomer(customer) //성함 입력
        setRoomNum(customer) //방번호 입력
        setCheckIO(customer) //체크인&체크아웃

        println("호텔예약이 완료되었습니다.")

        setMoney(customer) //자금 설정

        customerList.add(customer) //리스트에 고객님 정보 입력완료!
        println()

    }

    //2.예약목록 출력
    fun reservationList(){
        if(customerList.isEmpty()){
            println("예약된 손님들이 없습니다!")
            println("")
            return
        }

        printList(customerList) // 리스트 보여주기
    }

    //3.예약목록 (정렬) 출력
    fun sortedReservationList(){
        if(customerList.isEmpty()){
            println("예약된 손님들이 없습니다!")
            println("")
            return
        }
        val temp = customerList.sortedBy{it.checkInDate}.toMutableList()
        printList(temp) //리스트 보여주기

    }

    //4.시스템 종료
    fun quitApp(){
        println("시스템이 종료 됩니다.")
        System.exit(0)
    }

    //5.금액 입금-출금 내역 목록 출력
    fun billList(){
        println("조회 하실 사용자 이름을 입력하세요")
        input = readLine()
        var isSearch = false
        for(li in customerList) {
            if (li.name == input) {
                println("#${li.roomNum}호 ${li.name} 고객님!")
                println("1. 초기 금액으로 ${li.initMoney} 원 입금되었습니다.")
                println("2. 예약금으로 ${li.fee} 원 출금되었습니다.")
                isSearch = true
            }
        }
        //찾지 못했을 경우
        if(!isSearch) println("예약된 사용자를 찾을 수 없습니다.")


        println()
    }

    //6.예약 변경/취소
    fun reservationChange(){

        println("예약을 변경할 사용자 이름을 입력하세요")
        input = readLine()

        ///input 값을 받아, 해당 되는 예약 목록 출력
        val name = input
        val temp = customerList.filter{it.name==name}.toMutableList()
        val tempSize = temp.count()
        if(temp.isEmpty()){//list가 비어있는경우

            println("사용자 이름으로 예약된 목록을 찾을 수 없습니다.")

        }
        else {
            while(true) {
                println("${name} 님이 예약한 목록입니다. 변경하실 예약번호를 입력해주세요(탈출은 exit 입력)")
                printList(temp)

                try {
                    input = readLine()
                    if (input == "exit") return //함수를 종료

                    var index = input?.toInt() //처리할 리스트인덱스값
                    if(index!! <= tempSize && index > 0) {
                        //변경 할건지 안할껀지 물어봄
                        println("해당 예약을 어떻게 하시겠어요? 1. 변경 2. 취소 / 이외 번호. 메뉴로 돌아가기")

                        input = readLine()
                        var inputInt =input?.toInt()

                        if(inputInt==1){

                            //  리스트의 있는 예약자를 불러오기
                            //  customerList 내의 값이 변경되어야 한다.
                            var cIndex = customerList.indexOf(temp[index-1])
                            //변경하는 함수 짜기
                            changeReservation(cIndex)
                            break
                        }
                        else if(inputInt==2){

                            var cIndex = customerList.indexOf(temp[index-1])

                            //취소하는 함수 짜기
                            cancelReservation(cIndex)
                            break
                        }

                    }
                    else println("잘못된 값 입력 하셨습니다.")
                }catch(e: Exception){
                    println("잘못된 값 입력 하셨습니다.")
                }

            }

        }

        println()
    }

    //예약 변경 함수
    fun changeReservation(index :Int){
        var customer = customerList[index]
        setRoomNum(customer) //방번호 입력
        setCheckIO(customer) //체크인&체크아웃

        println("예약이 변경되었습니다.")
    }

    //예약취소 함수
    fun cancelReservation(index :Int){
        customerList.removeAt(index)

        //문자열 출력
        println("[취소 유의사항]")
        println("체크인 3일 이전 취소 예약금 환불 불가")
        println("체크인 5일 이전 취소 예약금의 30% 환불")
        println("체크인 5일 이전 취소 예약금의 50% 환불")
        println("체크인 5일 이전 취소 예약금의 80% 환불")
        println("체크인 5일 이전 취소 예약금의 100% 환불")
        println("취소가 완료되었습니다.")

        //취소 하면 환불 해야하는데 설계를 잘못했네?
        //한명이 여려명 룸을 예약하는걸 고려 x

    }
    ///////////////////////////////////////////////////////////////////////////////

    //성함 입력
    fun setNameCustomer(c:Customer){
        while(true) {
            println("예약자분의 성함을 입력해주세요")
            input = readLine()
            if(input=="") continue
            c.name = input.toString()
            break
        }

    }

    //방번호 입력
    fun setRoomNum(c:Customer){
        var roomNum :Int?
        while(true) {
            //올바른 정수값이 들어오지않으면 예외처리
            try {
                println("예약할 방번호를 입력해주세요")
                input = readLine()
                roomNum = input?.toInt()!!
                if (roomNum >= 100 && roomNum <= 999){
                    c.roomNum =roomNum
                    break
                }
                else println("올바르지 않은 방번호 입니다. 방번호는 100~999 영역이내 입니다.")
            }catch(e:NumberFormatException){
                println("올바르지 않은 방번호 입니다. 방번호는 100~999 영역이내 입니다.")
            }
        }

    }

    //체크인 & 체크아웃
    fun setCheckIO(c: Customer){
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
//                    var parsedText = input?.substring(0..3)+"-"+
//                    input?.substring(4..5)+"-"+
//                    input?.substring(6..7)
//                    customer.checkInDate = LocalDate.parse(parsedText)

                    //입력된 값을 파싱!
                    var formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
                    c.checkInDate = LocalDate.parse(input,formatter)
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

                    //입력된 값을 파싱!
                    var formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
                    c.checkOutDate = LocalDate.parse(input,formatter)

                    if (c.checkInDate.isBefore(c.checkOutDate)) {
                        //예약이 가능한지 안하는지 판별
                        if (isReservaiton(c)) isCheck = true
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
    }

    //자금 설정
    fun setMoney(c :Customer){

        //이때 임의의 금액을 지급해주고 랜덤으로 호텔 예약비로 빠져나가도록
        c.customerSetMoneyRand()
        setFeeRand(c)

        //뺀값을 입력후 출력
        c.money -= c.fee
        println("예약자 분의 현재 자금  : ${c.money}")
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


    //예약 리스트 보여주기
    fun printList(li : MutableList<Customer>){
        for(c in li){
            print((li.indexOf(c)+1).toString()+". ")//1.
            print("사용자: "+c.name+", ")//사용자: 고객님,
            print("방번호: "+c.roomNum.toString()+"호, ")//방번호 : xxx호,
            print("체크인: "+c.checkInDate.toString()+", ")//2023-xx-xx .
            println("체크아웃: "+c.checkOutDate.toString())//2023-xx-xx >>>라인넘김
        }
    }

    //호텔 예약비 임의로 지정
    fun setFeeRand(customer :Customer){
        var range = (100000..1000000 step 10000)
        customer.fee = (range.first..range.last).random()
        println("호텔 예약비  : ${customer.fee}")
    }
}

//////////////////////////////////////////////////////////////////////////////////////////////////
//고객 클래스
class Customer{
    var name : String? ="" // 고객
    var roomNum : Int = -1 // 예약룸
    var checkInDate : LocalDate = LocalDate.of(2000,1,1)  //체크인 날짜
    var checkOutDate : LocalDate =LocalDate.of(2000,1,1) //체크아웃 날짜
    var fee = 0//호텔 예약비
    var money = 0 // 고객 지갑..
    var initMoney = 0 //초기 입금


    //고객에 임의의 머니를 지급
    fun customerSetMoneyRand(){
//        this.money= (100000..1000000).random()
        var range = (100000..1000000 step 1000)// 천원 단위로 랜덤값 주고싶어서..
        this.initMoney = (range.first..range.last).random()
        money = this.initMoney
        println("예약자 분의 초기 자금  : ${this.money}")
    }

}

fun main(){
    var a = HotelReservation()
    var input : String?
    while(true){
        a.executeApp()
    }
}