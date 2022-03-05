import com.nadimohammed.domain.Result
import com.nadimohammed.domain.entities.currencyrate.CurrencyRateEntitie
import com.nadimohammed.domain.entities.othercurrencyrate.OtherCurrencyRate
import com.nadimohammed.domain.repository.CurrencyRepository
import com.nadimohammed.domain.usecases.GetCurrencyRateUseCase
import com.nadimohammed.domain.usecases.GetOtherCurrenciesRateUseCase
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test


@ExperimentalCoroutinesApi
internal class CurrenciesRateUseCaseTest {

    @Test
    fun `CurrencyRepository() then invoke GetCurrencyRateUseCase from CurrencyRepository`() =
        runBlockingTest {
            try {

                var invoked = false

                //Arrange
                val currencyRepository = object : CurrencyRepository {
                    override suspend fun getCurrencyRate(): com.nadimohammed.domain.Result<CurrencyRateEntitie> {
                        invoked = true
                        return com.nadimohammed.domain.Result.Success(CurrencyRateEntitie())
                    }

                    override suspend fun getOtherCurrencyRate(): Result<OtherCurrencyRate> {
                        TODO("Not yet implemented")
                    }
                }

                //Act
                GetCurrencyRateUseCase(currencyRepository).getCurrencyRate()

                //Assert
                assert(invoked)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    @Test
    fun `CurrencyRepository() with successful getCurrencyRate() from GetCurrencyRateUseCase then return currency rate`() =
        try {
            runBlockingTest {

                //Arrange
                val currencyRepository = object : CurrencyRepository {
                    override suspend fun getCurrencyRate(): Result<CurrencyRateEntitie> {
                        return Result.Success(CurrencyRateEntitie())
                    }

                    override suspend fun getOtherCurrencyRate(): Result<OtherCurrencyRate> {
                        TODO("Not yet implemented")
                    }
                }

                //Act
                val result = GetCurrencyRateUseCase(currencyRepository).getCurrencyRate()

                //Assert
                val expected = Result.Success(CurrencyRateEntitie())

                assertEquals(expected, result)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


    @Test
    fun `CurrencyRepository() then invoke GetOtherCurrenciesRateUseCase from CurrencyRepository`() =
        runBlockingTest {
            try {

                var invoked = false

                //Arrange
                val currencyRepository = object : CurrencyRepository {
                    override suspend fun getCurrencyRate(): com.nadimohammed.domain.Result<CurrencyRateEntitie> {
                        TODO("Not yet implemented")
                    }

                    override suspend fun getOtherCurrencyRate(): Result<OtherCurrencyRate> {
                        invoked = true
                        return Result.Success(OtherCurrencyRate())

                    }
                }

                //Act
                GetOtherCurrenciesRateUseCase(currencyRepository).getOtherCurrencyRate()

                //Assert
                assert(invoked)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    @Test
    fun `CurrencyRepository() with successful getOtherCurrencyRate() from GetOtherCurrenciesRateUseCase then return currency rate`() =
        try {
            runBlockingTest {

                //Arrange
                val currencyRepository = object : CurrencyRepository {
                    override suspend fun getCurrencyRate(): Result<CurrencyRateEntitie> {
                        TODO("Not yet implemented")
                    }

                    override suspend fun getOtherCurrencyRate(): Result<OtherCurrencyRate> {
                        return Result.Success(OtherCurrencyRate())
                    }
                }

                //Act
                val result =
                    GetOtherCurrenciesRateUseCase(currencyRepository).getOtherCurrencyRate()

                //Assert
                val expected = Result.Success(OtherCurrencyRate())

                assertEquals(expected, result)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
}
