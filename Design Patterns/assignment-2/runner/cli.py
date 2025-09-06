from __future__ import annotations

import uvicorn
from dotenv import load_dotenv
from typer import Typer

from app.runner.constants import DATABASE_NAME
from app.runner.setup import init_app

cli = Typer(no_args_is_help=True, add_completion=False)


@cli.command()
def run(host: str = "127.0.0.1", port: int = 9000) -> None:
    load_dotenv()

    uvicorn.run(host=host, port=port, app=init_app(DATABASE_NAME))
