from __future__ import annotations

from typing import Any, no_type_check
from uuid import UUID

from fastapi import APIRouter, HTTPException
from starlette.responses import JSONResponse

from app.core.Models.product import CreateProductRequest, UpdateProductRequest
from app.core.product import ProductService
from app.infrastructure.fastapi.dependables import ProductRepositoryDependable

product_api: APIRouter = APIRouter()


@product_api.get(
    "/products/{product_id}", status_code=200, response_model=dict[str, Any]
)
@no_type_check
def read_product(
    product_id: UUID, products: ProductRepositoryDependable
) -> dict[str, Any] | JSONResponse:
    try:
        return {"product": ProductService(products).read(product_id)}
    except ValueError as e:
        return JSONResponse(
            status_code=404,
            content={"error": {"message": str(e)}},
        )


@product_api.post("/products", status_code=201, response_model=dict[str, Any])
@no_type_check
def create_product(
    request: CreateProductRequest, products: ProductRepositoryDependable
) -> dict[str, Any] | JSONResponse:
    try:
        return {"product": ProductService(products).create(request)}
    except ValueError as e:
        raise HTTPException(status_code=409, detail={"error": {"message": str(e)}})


@product_api.get("/products", status_code=200, response_model=dict[str, Any])
@no_type_check
def read_all_products(
    products: ProductRepositoryDependable,
) -> dict[str, Any]:
    return {"products": ProductService(products).read_all()}


@product_api.patch("/products/{product_id}", status_code=200, response_model=None)
@no_type_check
def update_products(
    request: UpdateProductRequest,
    product_id: UUID,
    products: ProductRepositoryDependable,
) -> None:
    try:
        ProductService(products).update_product(request, product_id)
        return {"product updated"}
    except ValueError as e:
        raise HTTPException(status_code=409, detail={"error": {"message": str(e)}})
