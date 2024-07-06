package second_in_command

import com.fs.starfarer.api.Global
import second_in_command.specs.SCOfficer

object SCUtils {

    var MOD_ID = "second_in_command"
    var DATA_KEY = "\$sc_stored_data"

    @JvmStatic
    fun getSCData() : SCData {
        var data = Global.getSector().characterData.memoryWithoutUpdate.get(DATA_KEY) as SCData?
        if (data == null) {
            data = SCData(Global.getSector().characterData.person)
            Global.getSector().characterData.memoryWithoutUpdate.set(DATA_KEY, data)
        }
        return data
    }

    @JvmStatic
    fun createRandomSCOfficer(aptitudeId: String) : SCOfficer {
        var person = Global.getSector().playerFaction.createRandomPerson()
        var officer = SCOfficer(person, aptitudeId)
        return officer
    }

    @JvmStatic
    fun isSkillActive(skillId: String) : Boolean {
        return getSCData().isSkillActive(skillId)
    }

    @JvmStatic
    fun computeThresholdBonus(current: Float, maxBonus: Float, maxThreshold: Float): Float {

        var bonus = 0f
        var currValue = current
        var threshold = maxThreshold

        bonus = getThresholdBasedRoundedBonus(maxBonus, currValue, threshold)
        return bonus
    }

    private fun getThresholdBasedRoundedBonus(maxBonus: Float, value: Float, threshold: Float): Float {
        var bonus = maxBonus * threshold / Math.max(value, threshold)
        if (bonus > 0 && bonus < 1) bonus = 1f
        if (maxBonus > 1f) {
            if (bonus < maxBonus) {
                bonus = Math.min(bonus, maxBonus - 1f)
            }
            bonus = Math.round(bonus).toFloat()
        }
        return bonus
    }

}