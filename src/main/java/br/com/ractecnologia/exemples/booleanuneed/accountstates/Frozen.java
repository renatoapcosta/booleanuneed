package br.com.ractecnologia.exemples.booleanuneed.accountstates;

import br.com.ractecnologia.exemples.booleanuneed.AccountState;
import br.com.ractecnologia.exemples.booleanuneed.AccountUnfrozen;

import java.math.BigDecimal;
import java.util.function.Consumer;

public class Frozen implements AccountState {
    private AccountUnfrozen onUnfrozen;

    public Frozen(AccountUnfrozen onUnfrozen) {
        this.onUnfrozen = onUnfrozen;
    }

    @Override
    public AccountState deposit(BigDecimal amount, Consumer<BigDecimal> addToBalance) {
        addToBalance.accept(amount);
        return this.unfreeze();
    }

    @Override
    public AccountState withdraw(BigDecimal balance, BigDecimal amount,
                                 Consumer<BigDecimal> subtractFromBalance) {
        if (balance.compareTo(amount) >= 0) {
            subtractFromBalance.accept(amount);
        }
        return this.unfreeze();
    }

    private AccountState unfreeze() {
        this.onUnfrozen.handle();
        return new Active(this.onUnfrozen);
    }

    @Override
    public AccountState freezeAccount() {
        return this;
    }

    @Override
    public AccountState holderVerified() {
        return this;
    }

    @Override
    public AccountState closeAccount() {
        return new Closed();
    }
}
