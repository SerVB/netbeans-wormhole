package io.github.servb.netbeans.wormhole

import com.jetbrains.wormhole.events.WormholeEvent
import com.jetbrains.wormhole.interop.WindowId
import com.jetbrains.wormhole.interop.windows.WindowsNativeApplicationScanner
import com.jetbrains.wormhole.swing.JWormhole
import org.openide.windows.TopComponent
import java.awt.BorderLayout

class WormholeTopComponent(private val windowId: WindowId) : TopComponent() {

    init {
        this.layout = BorderLayout()
    }

    private val wormhole by lazy {
        JWormhole().also { wormhole ->
            this.add(wormhole)
            wormhole.addEventListener(WormholeEvent.EmbeddedWindowDead) { this.close() }
        }
    }

    override fun componentOpened() {
        check(wormhole.isCompatiblePlatform) { "bad platform" }
        check(wormhole.canEmbedWindow(windowId)) { "bad window" }

        wormhole.embed(windowId)

        displayName = WindowsNativeApplicationScanner.getWindowInfo(windowId).name
    }

    override fun preferredID(): String = windowId.toString()
    override fun getPersistenceType(): Int = PERSISTENCE_NEVER
}
