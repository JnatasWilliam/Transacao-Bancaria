package com.jonatas.transacao.query.listener;

import com.jonatas.transacao.command.event.TransacaoCriadaEvent;
import com.jonatas.transacao.query.model.TransacaoLeitura;
import com.jonatas.transacao.query.repository.TransacaoLeituraRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class TransacaoLeituraListener {

    private final TransacaoLeituraRepository leituraRepository;

    public TransacaoLeituraListener(TransacaoLeituraRepository leituraRepository) {
        this.leituraRepository = leituraRepository;
    }

    @EventListener
    public void onTransacaoCriada(TransacaoCriadaEvent event) {
        TransacaoLeitura leitura = new TransacaoLeitura(
                event.id(),
                event.origem(),
                event.destino(),
                event.valor(),
                event.tipo(),
                event.criadaEm()
        );
        leituraRepository.save(leitura);
    }
}