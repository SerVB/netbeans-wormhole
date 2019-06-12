package io.github.servb.netbeans.wormhole

import com.jetbrains.wormhole.interop.windows.WindowsNativeApplicationScanner
import com.jetbrains.wormhole.swing.JWormhole
import org.openide.windows.WindowManager
import java.awt.BorderLayout
import java.awt.Frame
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.*

class WormholeAction : ActionListener {

    override fun actionPerformed(e: ActionEvent) {
        val wormhole = JWormhole()

        if (wormhole.isCompatiblePlatform) {
            showWindowChooser(wormhole)
        } else {
            showUnsupportedPlatformMethod()
        }
    }

    private fun showWindowChooser(wormhole: JWormhole) {
        val openedWindows = getOpenedWindowsAndNames()
        val openedWindowsNames = openedWindows.map { it.second }

        val dialog = JDialog(netBeansFrame, "Window chooser", true)

        val panel = JPanel(BorderLayout())
        val comboBox = JComboBox(openedWindowsNames.toTypedArray())
        panel.add(comboBox, BorderLayout.CENTER)

        val embedButton = JButton("Embed")
        embedButton.addActionListener {
            val windowIdToEmbed = openedWindows[comboBox.selectedIndex].first

            val wormholeTopComponent = WormholeTopComponent(windowIdToEmbed, wormhole)
            wormholeTopComponent.open()
            wormholeTopComponent.requestActive()

            dialog.dispose()
        }

        val buttons = arrayOf(embedButton)
        val optionPane = JOptionPane(
            panel,
            JOptionPane.QUESTION_MESSAGE,
            JOptionPane.DEFAULT_OPTION,
            null,
            buttons,
            embedButton
        )

        dialog.contentPane.add(optionPane)
        dialog.pack()
        dialog.setLocationRelativeTo(netBeansFrame)
        dialog.isResizable = false
        dialog.isVisible = true
    }

    private fun showUnsupportedPlatformMethod() {
        JOptionPane.showMessageDialog(
            netBeansFrame,
            "Your OS isn't supported by Wormhole yet.",
            "Unsupported platform",
            JOptionPane.ERROR_MESSAGE
        )
    }

    private fun getOpenedWindowsAndNames() = WindowsNativeApplicationScanner
        .getOpenedWindows()
        .map { id -> id to WindowsNativeApplicationScanner.getWindowInfo(id, iconNeeded = false) }
        .sortedBy { (_, info) -> info.name }

    private val netBeansFrame: Frame get() = WindowManager.getDefault().mainWindow
}
