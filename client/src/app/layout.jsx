import { Inter } from "next/font/google";
import "./globals.css";
import { UserProvider } from "./context/UserProvider";
import { ChatProvider } from "./context/ChatProvider";

const inter = Inter({ subsets: ["latin"] });

export const metadata = {
  title: "Chat",
  charSet: "UTF-8",
};

export default function RootLayout({ children }) {
  return (
    <html lang="en">
      <body className={inter.className}>
        <UserProvider>
          <ChatProvider>{children}</ChatProvider>
        </UserProvider>
      </body>
    </html>
  );
}
