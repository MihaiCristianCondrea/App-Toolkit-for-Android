import importlib.util
from pathlib import Path


TRENDS_PATH = Path(__file__).resolve().with_name("trends.py")
spec = importlib.util.spec_from_file_location("trends_compat", TRENDS_PATH)
trends = importlib.util.module_from_spec(spec)
spec.loader.exec_module(trends)

globals().update(
    {
        name: value
        for name, value in trends.__dict__.items()
        if not name.startswith("_")
    }
)

main = trends.main


if __name__ == "__main__":
    main()
