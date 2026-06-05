package br.com.fiap.rocketlaunch.service.validation;

import br.com.fiap.rocketlaunch.domain.entity.Launch;
import br.com.fiap.rocketlaunch.exception.BusinessException;
import org.springframework.stereotype.Component;

@Component
public class WeatherValidator implements LaunchValidator {

    @Override
    public void validate(Launch launch) {
        if (launch.getWeatherCondition() == null) {
            throw new BusinessException(
                    "Lançamento deve ter condição climática registrada."
            );
        }
        if (!launch.getWeatherCondition().isSafeForLaunch()) {
            throw new BusinessException(
                    "Condições climáticas inadequadas para lançamento. " +
                            "Verifique: vento (máx 65 km/h), visibilidade (mín 5 km), " +
                            "precipitação (máx 10 mm)."
            );
        }
    }
}