package de.apnmt.payment.common.service.stripe.mapper;

import com.stripe.model.PaymentMethod;
import de.apnmt.payment.common.service.dto.PaymentMethodDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentMethodMapper {

    public PaymentMethodDTO toDTO(PaymentMethod paymentMethod) {
        PaymentMethodDTO paymentMethodDTO = new PaymentMethodDTO();
        paymentMethodDTO.setId(paymentMethod.getId());
        paymentMethodDTO.setBrand(paymentMethod.getCard().getBrand());
        paymentMethodDTO.setLast4(paymentMethod.getCard().getLast4());
        return paymentMethodDTO;
    }

    public List<PaymentMethodDTO> toDTO(List<PaymentMethod> paymentMethods) {
        List<PaymentMethodDTO> paymentMethodDTOS = new ArrayList<>(paymentMethods.size());
        for (PaymentMethod paymentMethod : paymentMethods) {
            paymentMethodDTOS.add(toDTO(paymentMethod));
        }
        return paymentMethodDTOS;
    }

}
