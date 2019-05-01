package io.github.servb.netbeans.wormhole

import com.jetbrains.wormhole.interop.windows.WindowsNativeApplicationScanner
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

class WormholeAction : ActionListener {

    override fun actionPerformed(e: ActionEvent) {
        val notepadWindow = WindowsNativeApplicationScanner
            .getOpenedWindows("notepad")
            .first()

        WormholeTopComponent(notepadWindow).open()
    }
}
