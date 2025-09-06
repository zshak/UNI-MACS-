import pytest

from app.core.receipt import Receipt
from app.core.sales import Sales
from app.infra.repositories.database import DatabaseHandler
from app.infra.repositories.receipt_repository import SqlReceiptRepository
from app.infra.repositories.sales_repository import SqlSalesRepository
from app.runner.constants import RECEIPTS_TABLE_COLUMNS, TEST_DATABASE_NAME
from app.tests.fake import Fake


@pytest.fixture
def database() -> DatabaseHandler:
    db = DatabaseHandler(TEST_DATABASE_NAME)
    return db


def test_sales_repository_report_empty(database: DatabaseHandler) -> None:
    receipts = SqlReceiptRepository(
        database, "test_receipt_report", RECEIPTS_TABLE_COLUMNS
    )
    receipts.create()
    sales = Sales()

    sales_repository = SqlSalesRepository(database, "test_receipt_report")
    assert sales_repository.read() == sales


def test_sales_repository_report(database: DatabaseHandler) -> None:
    receipts = SqlReceiptRepository(
        database, "test_receipt_report", RECEIPTS_TABLE_COLUMNS
    )
    receipts.create()
    receipt = Receipt()
    receipts.add(receipt)
    product = Fake().product_in_receipt()
    receipts.add_product(receipt.get_id(), product)
    receipt.add_product(product)

    sales = Sales(1, product.get_total())

    sales_repository = SqlSalesRepository(database, "test_receipt_report")

    assert sales_repository.read() == sales
