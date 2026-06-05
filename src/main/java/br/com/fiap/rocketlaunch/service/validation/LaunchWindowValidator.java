package br.com.fiap.rocketlaunch.service.validation;

import br.com.fiap.rocketlaunch.domain.entity.Launch;
import br.com.fiap.rocketlaunch.domain.entity.LaunchWindow;
import br.com.fiap.rocketlaunch.exception.BusinessException;
import org.springframework.stereotype.Component;

@Component
public class LaunchWindowValidator implements LaunchValidator {

    @Override
    public void validate(Launch launch) {
        if (launch.getLaunchWindows() == null || launch.getLaunchWindows().isEmpty()) {
            throw new BusinessException(
                    "Lançamento deve ter ao menos uma janela de lançamento associada."
            );
        }

        boolean hasActiveWindow = launch.getLaunchWindows()
                .stream()
                .anyMatch(LaunchWindow::isActive);

        if (!hasActiveWindow) {
            throw new BusinessException(
                    "Nenhuma janela de lançamento está ativa no momento. " +
                            "Verifique as janelas disponíveis e tente novamente."
            );
        }
    }
}