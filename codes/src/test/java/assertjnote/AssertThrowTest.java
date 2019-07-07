package assertjnote;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Test;
import org.omg.SendingContext.RunTime;

public class AssertThrowTest {
    @Test
    public void testInstanceOf() {
        assertThatThrownBy(() -> { throw new InnerCauseException(); })
                .isInstanceOf(Exception.class);
    }

    @Test
    public void testHasCauseInstanceOf() {
        assertThatThrownBy(() -> {
            throw new HasCauseException(new InnerCauseException()); })
                .hasCauseInstanceOf(HasCauseException.class);
    }
}

class InnerCauseException extends RuntimeException {
}
class HasCauseException extends RuntimeException {
    HasCauseException(Throwable cause) {
        super(cause);
    }
}
