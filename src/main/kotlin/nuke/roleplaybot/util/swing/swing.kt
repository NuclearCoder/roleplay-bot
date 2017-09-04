package nuke.roleplaybot.util.swing

import nuke.roleplaybot.LOGGER
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper
import javax.swing.UIManager

/**
 * Created by NuclearCoder on 2017-08-25.
 */

fun trySetLNF() {
    try {
        BeautyEyeLNFHelper.launchBeautyEyeLNF()
        UIManager.put("RootPane.setupButtonVisible", false)
        BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.translucencySmallShadow
    } catch (e: Exception) {
        LOGGER.warn("Couldn't use bundled look-and-feel, falling back to system LNF...")
        // try system lnf
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
        } catch (e: Exception) {
            LOGGER.error("Couldn't use system look-and-feel.")
        }
    }
}