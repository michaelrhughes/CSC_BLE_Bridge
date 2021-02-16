package idv.markkuo.cscblebridge.service.ant

import idv.markkuo.cscblebridge.service.ble.BleServiceType

sealed class AntDevice(val deviceId: Int, val deviceName: String, val typeName: String, val bleType: BleServiceType) {
    data class BsdDevice(
            private val id: Int,
            private val name: String,
            var lastSpeed: Float,
            var cumulativeWheelRevolution: Long,
            var lastWheelEventTime: Int,
            var lastSpeedTimestamp: Long
    ): AntDevice(id, name, "ANT+ Bike Speed", BleServiceType.CscService) {
        override fun getDataString(): String {
            return "Speed: $lastSpeed, RPM: $cumulativeWheelRevolution"
        }
    }

    data class SSDevice(
            private val id: Int,
            private val name: String,
            var ssDistance: Long,
            var ssDistanceTimestamp: Long,
            var ssSpeed: Float,
            var ssSpeedTimestamp: Long,
            var stridePerMinute: Long,
            var stridePerMinuteTimestamp: Long
    ) : AntDevice(id, name, "ANT+ Stride SDM", BleServiceType.RscService) {
        override fun getDataString(): String {
            return "Speed: $ssSpeed, Stride/Min: $stridePerMinute"
        }
    }

    data class HRDevice(
            private val id: Int,
            private val name: String,
            var hr: Int,
            var hrTimestamp: Long
    ) : AntDevice(id, name, "ANT+ Heart Rate", BleServiceType.HrService) {
        override fun getDataString(): String {
            return "Heart Rate: $hr"
        }
    }

    abstract fun getDataString(): String
}