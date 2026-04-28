package at.hannibal2.skyhanni.events

import at.hannibal2.skyhanni.data.ContainerInput
import at.hannibal2.skyhanni.utils.SafeItemStack

// Left or right click into the world, with the item in hand
class ItemClickEvent(itemInHand: SafeItemStack?, clickType: ContainerInput) : WorldClickEvent(itemInHand, clickType)
