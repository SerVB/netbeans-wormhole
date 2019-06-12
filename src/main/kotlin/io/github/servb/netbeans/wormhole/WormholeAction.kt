package io.github.servb.netbeans.wormhole

import com.jetbrains.wormhole.interop.windows.WindowsNativeApplicationScanner
import com.jetbrains.wormhole.swing.JWormhole
import org.openide.windows.WindowManager
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.JOptionPane

class WormholeAction : ActionListener {

    override fun actionPerformed(e: ActionEvent) {
        val wormhole = JWormhole()

        if (wormhole.isCompatiblePlatform) {
            val notepadWindow = WindowsNativeApplicationScanner
                .getOpenedWindows("notepad")
                .first()

            val wormholeTopComponent = WormholeTopComponent(notepadWindow, wormhole)
            wormholeTopComponent.open()
            wormholeTopComponent.requestActive()
        } else {
            showUnsupportedPlatformMethod()
        }
    }

    private fun showUnsupportedPlatformMethod() {
        JOptionPane.showMessageDialog(
            WindowManager.getDefault().mainWindow,
            "Your OS isn't supported by Wormhole yet.",
            "Unsupported platform",
            JOptionPane.ERROR_MESSAGE
        )
    }
}
