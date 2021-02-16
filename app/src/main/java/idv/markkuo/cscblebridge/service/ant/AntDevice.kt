package idv.markkuo.cscblebridge.service.ant

sealed class AntDevice(val deviceId: Int, val deviceName: String, val typeName: String) {
    class BsdDevice(
            id: Int,
            name: String,
            var lastSpeed: Float,
            var cumulativeWheelRevolution: Long,
            var lastWheelEventTime: Int,
            var lastSpeedTimestamp: Long
    ): AntDevice(id, name, "ANT+ Bike Speed") {
        override fun getDataString(): String {
            return "Speed: $lastSpeed, RPM: $cumulativeWheelRevolution"
        }
    }

    class SSDevice(
            id: Int,
            name: String,
            var ssDistance: Long,
            var ssDistanceTimestamp: Long,
            var ssSpeed: Float,
            var ssSpeedTimestamp: Long,
            var stridePerMinute: Long,
            var stridePerMinuteTimestamp: Long
    ) : AntDevice(id, name, "ANT+ Stride SDM") {
        override fun getDataString(): String {
            return "Speed: $ssSpeed, Stride/Min: $stridePerMinute"
        }
    }

    class HRDevice(
            id: Int,
            name: String,
            var hr: Int,
            var hrTimestamp: Long
    ) : AntDevice(id, name, "ANT+ Heart Rate") {
        override fun getDataString(): String {
            return "Heart Rate: $hr"
        }
    }

    abstract fun getDataString(): String
}