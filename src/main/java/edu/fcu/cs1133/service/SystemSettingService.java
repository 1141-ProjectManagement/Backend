package edu.fcu.cs1133.service;

import edu.fcu.cs1133.model.SystemSetting;
import edu.fcu.cs1133.repository.SystemSettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SystemSettingService {

    @Autowired
    private SystemSettingRepository systemSettingRepository;

    @Transactional(readOnly = true)
    public Map<String, String> getSettings() {
        return systemSettingRepository.findAll().stream()
                .collect(Collectors.toMap(SystemSetting::getKey, SystemSetting::getValue));
    }

    @Transactional
    public void updateSettings(Map<String, String> settings) {
        settings.forEach((key, value) -> {
            SystemSetting setting = systemSettingRepository.findById(key)
                    .orElse(new SystemSetting());
            setting.setKey(key);
            setting.setValue(value);
            systemSettingRepository.save(setting);
        });
    }
}
