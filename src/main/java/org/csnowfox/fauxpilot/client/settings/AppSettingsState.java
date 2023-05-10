package org.csnowfox.fauxpilot.client.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
        name = "org.csnowfox.fauxpilot.client.settings.AppSettingsState",
        storages = @Storage("fauxpilotClientSettingsPlugin.xml")
)
public class AppSettingsState implements PersistentStateComponent<AppSettingsState> {

    public String serverUrl = "http://192.168.174.64:5000/v1/engines/codegen/completions";

    public static AppSettingsState getInstance() {
        return ApplicationManager.getApplication().getService(AppSettingsState.class);
    }

    @Nullable
    @Override
    public AppSettingsState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull AppSettingsState state) {
        XmlSerializerUtil.copyBean(state, this);
    }

}

