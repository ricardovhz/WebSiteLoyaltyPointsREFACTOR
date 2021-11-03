package website.loyaltypoints.service;

public class CursoNaoPossueVagasException extends RuntimeException {
    public CursoNaoPossueVagasException() {
        super("Curso Não possui vagas");
    }
}
