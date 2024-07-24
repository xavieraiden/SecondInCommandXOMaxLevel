package second_in_command.skills.technology

import com.fs.starfarer.api.combat.MutableShipStatsAPI
import com.fs.starfarer.api.combat.ShipAPI
import com.fs.starfarer.api.combat.ShipVariantAPI
import com.fs.starfarer.api.impl.campaign.ids.Stats
import com.fs.starfarer.api.ui.TooltipMakerAPI
import com.fs.starfarer.api.util.Misc
import second_in_command.specs.SCBaseSkillPlugin

class DeepDive : SCBaseSkillPlugin() {

    override fun getAffectsString(): String {
        return "all phase ships"
    }

    override fun addTooltip(tooltip: TooltipMakerAPI) {

        tooltip.addPara("The active timeflow from phase-cloaks is increased by a flat 100%%", 0f, Misc.getHighlightColor(), Misc.getHighlightColor())
        tooltip.addPara("   - The base increase in timeflow from a standard phase system is 300%%", 0f, Misc.getTextColor(), Misc.getHighlightColor(), "300%")

    }

    override fun applyEffectsBeforeShipCreation(stats: MutableShipStatsAPI?, variant: ShipVariantAPI, hullSize: ShipAPI.HullSize?, id: String?) {

        stats!!.dynamic.getStat(Stats.PHASE_TIME_BONUS_MULT).modifyFlat(id, 0.5f)

    }

    override fun applyEffectsAfterShipCreation(ship: ShipAPI?, variant: ShipVariantAPI, id: String?) {



    }



}