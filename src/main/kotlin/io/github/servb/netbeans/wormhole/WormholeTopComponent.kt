package io.github.servb.netbeans.wormhole

import com.jetbrains.wormhole.events.WormholeEvent
import com.jetbrains.wormhole.interop.WindowId
import com.jetbrains.wormhole.interop.windows.WindowsNativeApplicationScanner
import com.jetbrains.wormhole.swing.JWormhole
import org.openide.windows.TopComponent
import java.awt.BorderLayout

class WormholeTopComponent(private val windowId: WindowId, private val wormhole: JWormhole) : TopComponent() {

    init {
        this.layout = BorderLayout()
        this.add(wormhole)
        wormhole.addEventListener(WormholeEvent.EmbeddedWindowDead) { this.close() }
    }

    override fun componentOpened() {
        check(wormhole.canEmbedWindow(windowId)) { "bad window" }

        wormhole.embed(windowId)

        displayName = WindowsNativeApplicationScanner.getWindowInfo(windowId).name
    }

    override fun componentClosed() {
        wormhole.unembed()
    }

    override fun preferredID(): String = windowId.toString()
    override fun getPersistenceType(): Int = PERSISTENCE_NEVER
}
