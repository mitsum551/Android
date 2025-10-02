import kotlin.random.Random
import kotlin.math.*

class Human
{
    var fio: String = ""
    var age: Int = 0
    var currentSpeed: Int = 0
    var group_number: Int = -1
    var x: Int = 0
    var y: Int = 0
    constructor(_name: String, _age: Int, _speed: Int, _gn: Int){
        fio = _name
        age = _age
        currentSpeed = _speed
        group_number = _gn
    }

    fun getFullName(): String {
        return "$fio"
    }

    fun move()
    {
        val angle = Random.nextDouble(0.0, 2 * PI)
        val distance = Random.nextInt(0, currentSpeed + 1)

        x += (distance * cos(angle)).toInt()
        y += (distance * sin(angle)).toInt()
    }

    fun moveTo(_toX: Int, _toY: Int)
    {
        x = _toX
        y = _toY
    }
}

fun main(){
    val people = arrayOf(
        Human("Плотников Роман Анатольевич", 40, 5, 444),
        Human("Паровозов Аркадий Вадимович", 13, 4, 442),
        Human("Кит Максим Аркадьевич", 19, 6, 443),
        Human("Плотникова Наталья Анатольевна", 37, 3, 441)
    )
    val simulationTime = 10
    for (second in 1..simulationTime) {
        println("\n$second c.:")
        for (person in people) {
            person.move()
            println("${person.fio} переместился в (${person.x}, ${person.y})")
        }
        Thread.sleep(1000)
    }
    println("\nФинальные позиции:")
    for (person in people) {
        println("${person.getFullName()}: (${person.x}, ${person.y})")
    }
}