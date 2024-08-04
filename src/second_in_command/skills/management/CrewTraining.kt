package second_in_command.skills.management

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.combat.MutableShipStatsAPI
import com.fs.starfarer.api.combat.ShipAPI
import com.fs.starfarer.api.combat.ShipVariantAPI
import com.fs.starfarer.api.ui.TooltipMakerAPI
import com.fs.starfarer.api.util.Misc
import org.lazywizard.lazylib.MathUtils
import second_in_command.SCData
import second_in_command.SCUtils.addOrCheckTag
import second_in_command.specs.SCBaseSkillPlugin

class CrewTraining : SCBaseSkillPlugin() {


    companion object {
        fun removeOfficersOverTheLimit() {
            var player = Global.getSector().characterData.person
            player.stats.officerNumber.unmodify("sc_crew_training")

            var maximum = player.stats.officerNumber.modifiedValue

            var fleet = Global.getSector().playerFleet
            var shipsWithOfficers = fleet.fleetData.membersListCopy.filter { fleet.fleetData.officersCopy.map { it.person }.contains(it.captain) }.toMutableList()

            while (shipsWithOfficers.count() > maximum) {
                if (shipsWithOfficers.isEmpty()) break
                var last = shipsWithOfficers.last()
                last.captain = null
                shipsWithOfficers.remove(last)
            }
        }
    }

    override fun getAffectsString(): String {
        return "fleet"
    }

    override fun addTooltip(data: SCData, tooltip: TooltipMakerAPI) {

        tooltip.addPara("+10%% maximum combat readiness for all ships", 0f, Misc.getHighlightColor(), Misc.getHighlightColor())
        tooltip.addPara("   - The bonus is increased by 5%% for any ship with an officer assigned to it", 0f, Misc.getTextColor(), Misc.getHighlightColor(), "5%")

        tooltip.addSpacer(10f)

        tooltip.addPara("+2 to maximum number of officers you're able to command", 0f, Misc.getHighlightColor(), Misc.getHighlightColor())
        tooltip.addPara("   - If this executive officer is unassigned, any officer over the limit will also be unassigned", 0f, Misc.getTextColor(), Misc.getHighlightColor(), "5%")


    }

    override fun applyEffectsBeforeShipCreation(data: SCData, stats: MutableShipStatsAPI?, variant: ShipVariantAPI, hullSize: ShipAPI.HullSize?, id: String?) {
        var cr = 0.10f

        var captain = stats!!.fleetMember?.captain
        if (captain != null && !captain.isDefault && !captain.isAICore) {
            cr += 0.05f
        }

        stats!!.maxCombatReadiness.modifyFlat("sc_crew_training", cr, "Crew Training")

        if (data.isNPC && !variant.addOrCheckTag("sc_crew_training")) {
            stats.fleetMember.repairTracker.cr += cr
            stats.fleetMember.repairTracker.cr = MathUtils.clamp(stats.fleetMember.repairTracker.cr, 0f, 1f)
        }
    }

    override fun applyEffectsAfterShipCreation(data: SCData, ship: ShipAPI?, variant: ShipVariantAPI, id: String?) {

    }

    override fun advance(data: SCData, amount: Float) {
        data.commander.stats.officerNumber.modifyFlat("sc_crew_training", 2f)
    }

    override fun onActivation(data: SCData) {

        //Put CR at max
      /*  if (data.isNPC) {
            for (member in data.fleet.fleetData.membersListCopy) {

                var stats = member.stats ?: continue

                var cr = 0.10f

                var captain = stats!!.fleetMember?.captain
                if (captain != null && !captain.isDefault && !captain.isAICore) {
                    cr += 0.05f
                }

                stats!!.maxCombatReadiness.modifyFlat("sc_crew_training", cr, "Crew Training")
                member.repairTracker.cr += cr
            }
        }*/
    }

    override fun onDeactivation(data: SCData) {
        data.commander.stats.officerNumber.unmodify("sc_crew_training")

        if (!data.isNPC) {
            removeOfficersOverTheLimit()
        }
    }
}