package org.editor4j.gui;


import org.editor4j.gui.components.JBaseDialog;
import org.editor4j.gui.components.JField;
import org.editor4j.gui.components.JFontBox;
import org.editor4j.gui.styles.DarkStyle;
import org.editor4j.gui.styles.LightStyle;
import org.editor4j.gui.styles.MetalStyle;
import org.editor4j.models.Settings;
import org.editor4j.models.Style;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import static org.editor4j.managers.SettingsManager.currentSettings;

public class SettingsDialog extends JBaseDialog {

    public JButton apply = new JButton("Apply");

    JSpinner tabSizes = new JSpinner(new SpinnerNumberModel(4, 2, 10, 1));
    JCheckBox wordWrap = new JCheckBox();


    JComboBox<Style> styles = new JComboBox<>();

    JFontBox fonts = new JFontBox(15, Font.PLAIN, GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts());
    JSpinner fontSizes = new JSpinner(new SpinnerNumberModel(20, 10, 50, 1));


    JPanel jPanel = new JPanel();

    
    public SettingsDialog(){
        //Simple change just to test Git
        super("All Settings", 600, 440);
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));


        JTabbedPane jTabbedPane = new JTabbedPane();

        jTabbedPane.add("General", buildGeneralOptions());
        jTabbedPane.add("Appearance", buildStyleOptions());
        jTabbedPane.add("Fonts", buildFontOptions());

        jPanel.add(jTabbedPane);
        SwingUtilities.getRootPane(this).setDefaultButton(apply);
        setModalityType(ModalityType.APPLICATION_MODAL);

        super.setContent(jPanel);
        super.setDefaultButtonOnly(apply);


        apply.addActionListener(new ReloadSettingsDialogListener(this));

        setSettings(currentSettings);
    }


    private JPanel buildGeneralOptions() {
        JPanel jPanel = new JPanel();
        jPanel.add(new JField("Tab Size", tabSizes));
        jPanel.add(new JField("Enable Word Wrapping", wordWrap));



        return jPanel;
    }

    private JPanel buildStyleOptions() {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));

        try {
            styles.addItem(new LightStyle());
            styles.addItem(new DarkStyle());
            styles.addItem(new MetalStyle());

        } catch (IOException e){
            e.printStackTrace();
        }

        jPanel.add(new JField("Style", styles));

        return jPanel;
    }

    private JPanel buildFontOptions(){
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout());

        jPanel.add(new JField("Font", fonts));
        jPanel.add(new JField("Size", fontSizes));
        return jPanel;
    }

    private void setSettings(Settings currentSettings) {
        styles.getModel().setSelectedItem(currentSettings.style);
        fonts.getModel().setSelectedItem(currentSettings.font.getFontName());
        fontSizes.setValue(currentSettings.font.getSize());
        wordWrap.setSelected(currentSettings.wordWrapEnabled);
        tabSizes.setValue(currentSettings.tabSize);
    }





    //Get all the values from the GUI controls into a Settings object
    public Settings getSettings() throws IOException {
        Settings settings = new Settings();
        settings.style = (Style) styles.getSelectedItem();
        int fontSize = (int) fontSizes.getValue();
        String fontName = (String) fonts.getSelectedItem();

        settings.font = new Font(fontName, Font.PLAIN, fontSize);
        settings.wordWrapEnabled = wordWrap.isSelected();
        settings.tabSize = (int) tabSizes.getValue();
        return settings;
    }

    private class ReloadSettingsDialogListener implements ActionListener {
        private final SettingsDialog settingsDialog;

        public ReloadSettingsDialogListener(SettingsDialog settingsDialog) {
            this.settingsDialog = settingsDialog;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.updateComponentTreeUI(settingsDialog);
        }
    }
}
